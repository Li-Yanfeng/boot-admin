package org.utility.modules.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.base.impl.ServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.modules.mnt.mapper.DeployMapper;
import org.utility.modules.mnt.mapper.DeployServerMapper;
import org.utility.modules.mnt.mapper.ServerMapper;
import org.utility.modules.mnt.model.*;
import org.utility.modules.mnt.service.*;
import org.utility.modules.mnt.service.dto.AppDTO;
import org.utility.modules.mnt.service.dto.DeployDTO;
import org.utility.modules.mnt.service.dto.DeployQuery;
import org.utility.modules.mnt.service.dto.ServerDTO;
import org.utility.modules.mnt.util.ExecuteShellUtils;
import org.utility.modules.mnt.util.ScpClientUtils;
import org.utility.modules.mnt.websocket.MsgType;
import org.utility.modules.mnt.websocket.SocketMsg;
import org.utility.modules.mnt.websocket.WebSocketServer;
import org.utility.util.ConvertUtils;
import org.utility.util.FileUtils;
import org.utility.util.QueryHelp;
import org.utility.util.SecurityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 部署管理 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Service
public class DeployServiceImpl extends ServiceImpl<DeployMapper, DeployDTO, DeployQuery, Deploy> implements DeployService {

    /**
     * 循环次数
     */
    private final Integer count = 30;

    private final String FILE_SEPARATOR = "/";

    private final DeployMapper deployMapper;
    private final ServerMapper serverMapper;
    private final AppService appService;
    private final ServerService serverService;
    private final DeployHistoryService deployHistoryService;
    private final DeployServerService deployServerService;
    private final DeployServerMapper deployServerMapper;

    public DeployServiceImpl(DeployMapper deployMapper, ServerMapper serverMapper, AppService appService,
                             ServerService serverService, DeployHistoryService deployHistoryService,
                             DeployServerService deployServerService, DeployServerMapper deployServerMapper) {
        this.deployMapper = deployMapper;
        this.serverMapper = serverMapper;
        this.appService = appService;
        this.serverService = serverService;
        this.deployHistoryService = deployHistoryService;
        this.deployServerService = deployServerService;
        this.deployServerMapper = deployServerMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(DeployDTO resource) {
        Deploy deploy = ConvertUtils.convert(resource, Deploy.class);
        deploy.setAppId(resource.getApp().getAppId());
        deployMapper.insert(deploy);
        if (deploy.getDeployId() != null) {
            deployServerService.removeByDeployIds(CollUtil.newHashSet(deploy.getDeployId()));
        }
        for (ServerDTO server : resource.getDeploys()) {
            DeployServer ds = new DeployServer();
            ds.setDeployId(deploy.getDeployId());
            ds.setServerId(server.getServerId());
            deployServerMapper.insert(ds);
        }
    }

    @Override
    public void removeByIds(Collection<Long> ids) {
        deployServerService.removeByDeployIds(ids);
        deployMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateById(DeployDTO resource) {
        Deploy deploy = ConvertUtils.convert(resource, Deploy.class);
        deploy.setAppId(resource.getApp().getAppId());
        deployMapper.updateById(deploy);
        if (deploy.getDeployId() != null) {
            deployServerService.removeByDeployIds(CollUtil.newHashSet(deploy.getDeployId()));
        }
        for (ServerDTO server : resource.getDeploys()) {
            DeployServer ds = new DeployServer();
            ds.setDeployId(deploy.getDeployId());
            ds.setServerId(server.getServerId());
            deployServerMapper.insert(ds);
        }
    }

    @Override
    public List<DeployDTO> list(DeployQuery query) {
        List<DeployDTO> list = ConvertUtils.convertList(deployMapper.selectList(QueryHelp.queryWrapper(query)),
                DeployDTO.class);
        for (DeployDTO dd : list) {
            dd.setApp(appService.getById(dd.getAppId()));

            LambdaQueryWrapper<Server> wrapper = Wrappers.lambdaQuery();
            wrapper.in(Server::getServerId, deployServerService.listServerIdByDeployId(dd.getDeployId()));
            dd.setDeploys(new HashSet<>(ConvertUtils.convertList(serverMapper.selectList(wrapper), ServerDTO.class)));
        }
        return list;
    }

    @Override
    public IPage<DeployDTO> page(DeployQuery query) {
        IPage<Deploy> pageList = deployMapper.selectPage(QueryHelp.page(query), QueryHelp.queryWrapper(query));
        IPage<DeployDTO> page = ConvertUtils.convertPage(pageList, DeployDTO.class);
        for (DeployDTO dd : page.getRecords()) {
            dd.setApp(appService.getById(dd.getAppId()));

            LambdaQueryWrapper<Server> wrapper = Wrappers.lambdaQuery();
            wrapper.in(Server::getServerId, deployServerService.listServerIdByDeployId(dd.getDeployId()));
            dd.setDeploys(new HashSet<>(ConvertUtils.convertList(serverMapper.selectList(wrapper), ServerDTO.class)));
        }
        return page;
    }

    @Override
    public void deploy(String fileSavePath, Long appId) {
        this.deployApp(fileSavePath, appId);
    }

    @Override
    public String startServer(DeployDTO resource) {
        Set<ServerDTO> deploys = resource.getDeploys();
        AppDTO app = resource.getApp();
        for (ServerDTO deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(deploy.getIp());
            //为了防止重复启动，这里先停止应用
            this.stopApp(app.getPort(), executeShellUtil);
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            this.sendMsg("下发启动命令", MsgType.INFO);
            executeShellUtil.execute(app.getStartScript());
            sleep(3);
            this.sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看运行状态", MsgType.INFO);
            int i = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = this.checkIsRunningStatus(app.getPort(), executeShellUtil);
                if (result) {
                    break;
                }
                // 休眠6秒
                sleep(6);
            }
            sendResultMsg(result, sb);
            logger.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    @Override
    public String stopServer(DeployDTO resource) {
        Set<ServerDTO> deploys = resource.getDeploys();
        AppDTO app = resource.getApp();
        for (ServerDTO deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(deploy.getIp());
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            this.sendMsg("下发停止命令", MsgType.INFO);
            //停止应用
            this.stopApp(app.getPort(), executeShellUtil);
            sleep(1);
            boolean result = this.checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>关闭失败!");
                this.sendMsg(sb.toString(), MsgType.ERROR);
            } else {
                sb.append("<br>关闭成功!");
                this.sendMsg(sb.toString(), MsgType.INFO);
            }
            logger.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    @Override
    public String serverStatus(DeployDTO resource) {
        Set<ServerDTO> Servers = resource.getDeploys();
        AppDTO app = resource.getApp();
        for (ServerDTO Server : Servers) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(Server.getIp());
            sb.append("服务器:").append(Server.getName()).append("<br>应用:").append(app.getName());
            boolean result = this.checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>正在运行");
                this.sendMsg(sb.toString(), MsgType.INFO);
            } else {
                sb.append("<br>已停止!");
                this.sendMsg(sb.toString(), MsgType.ERROR);
            }
            logger.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    @Override
    public String serverReduction(DeployHistory resource) {
        Long deployId = resource.getDeployId();
        Deploy deployInfo = deployMapper.selectById(deployId);
        String deployDate = DateUtil.format(resource.getDeployDate(), DatePattern.PURE_DATETIME_PATTERN);
        AppDTO app = appService.getById(deployInfo.getAppId());
        if (app == null) {
            this.sendMsg("应用信息不存在：" + resource.getAppName(), MsgType.ERROR);
            throw new BadRequestException("应用信息不存在：" + resource.getAppName());
        }
        String backupPath = app.getBackupPath() + FILE_SEPARATOR;
        backupPath += resource.getAppName() + FILE_SEPARATOR + deployDate;
        //这个是服务器部署路径
        String deployPath = app.getDeployPath();
        String ip = resource.getIp();
        ExecuteShellUtils executeShellUtil = getExecuteShellUtils(ip);
        String msg;

        msg = String.format("登陆到服务器:%s", ip);
        logger.info(msg);
        this.sendMsg(msg, MsgType.INFO);
        this.sendMsg("停止原来应用", MsgType.INFO);
        //停止应用
        this.stopApp(app.getPort(), executeShellUtil);
        //删除原来应用
        this.sendMsg("删除应用", MsgType.INFO);
        executeShellUtil.execute("rm -rf " + deployPath + FILE_SEPARATOR + resource.getAppName());
        //还原应用
        this.sendMsg("还原应用", MsgType.INFO);
        executeShellUtil.execute("cp -r " + backupPath + "/. " + deployPath);
        this.sendMsg("启动应用", MsgType.INFO);
        executeShellUtil.execute(app.getStartScript());
        this.sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看启动状态", MsgType.INFO);
        int i = 0;
        boolean result = false;
        // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
        while (i++ < count) {
            result = this.checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                break;
            }
            // 休眠6秒
            sleep(6);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("服务器:").append(ip).append("<br>应用:").append(resource.getAppName());
        sendResultMsg(result, sb);
        executeShellUtil.close();
        return "";
    }

    @Override
    public void download(HttpServletResponse response, List<DeployDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (DeployDTO deploy : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(3, true);
            map.put("应用名称", deploy.getApp().getName());
            map.put("服务器", deploy.getServers());
            map.put("部署日期", deploy.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * @param fileSavePath 本机路径
     * @param id           ID
     */
    private void deployApp(String fileSavePath, Long id) {
        DeployDTO deploy = this.getById(id);
        if (deploy == null) {
            this.sendMsg("部署信息不存在", MsgType.ERROR);
            throw new BadRequestException("部署信息不存在");
        }
        AppDTO app = deploy.getApp();
        if (app == null) {
            this.sendMsg("包对应应用信息不存在", MsgType.ERROR);
            throw new BadRequestException("包对应应用信息不存在");
        }
        int port = app.getPort();
        //这个是服务器部署路径
        String uploadPath = app.getUploadPath();
        StringBuilder sb = new StringBuilder();
        String msg;
        Set<ServerDTO> deploys = deploy.getDeploys();
        for (ServerDTO deployDTO : deploys) {
            String ip = deployDTO.getIp();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(ip);
            //判断是否第一次部署
            boolean flag = this.checkFile(executeShellUtil, app);
            //第一步要确认服务器上有这个目录
            executeShellUtil.execute("mkdir -p " + app.getUploadPath());
            executeShellUtil.execute("mkdir -p " + app.getBackupPath());
            executeShellUtil.execute("mkdir -p " + app.getDeployPath());
            //上传文件
            msg = String.format("登陆到服务器:%s", ip);
            ScpClientUtils scpClientUtil = getScpClientUtils(ip);
            logger.info(msg);
            this.sendMsg(msg, MsgType.INFO);
            msg = String.format("上传文件到服务器:%s<br>目录:%s下，请稍等...", ip, uploadPath);
            this.sendMsg(msg, MsgType.INFO);
            scpClientUtil.putFile(fileSavePath, uploadPath);
            if (flag) {
                this.sendMsg("停止原来应用", MsgType.INFO);
                //停止应用
                this.stopApp(port, executeShellUtil);
                this.sendMsg("备份原来应用", MsgType.INFO);
                //备份应用
                backupApp(executeShellUtil, ip, app.getDeployPath() + FILE_SEPARATOR, app.getName(),
                        app.getBackupPath() + FILE_SEPARATOR, id);
            }
            this.sendMsg("部署应用", MsgType.INFO);
            //部署文件,并启动应用
            String deployScript = app.getDeployScript();
            executeShellUtil.execute(deployScript);
            sleep(3);
            this.sendMsg("应用部署中，请耐心等待部署结果，或者稍后手动查看部署状态", MsgType.INFO);
            int i = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = this.checkIsRunningStatus(port, executeShellUtil);
                if (result) {
                    break;
                }
                // 休眠6秒
                sleep(6);
            }
            sb.append("服务器:").append(deployDTO.getName()).append("<br>应用:").append(app.getName());
            sendResultMsg(result, sb);
            executeShellUtil.close();
        }
    }

    private void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void backupApp(ExecuteShellUtils executeShellUtil, String ip, String fileSavePath, String appName,
                           String backupPath, Long id) {
        String deployDate = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder sb = new StringBuilder();
        backupPath += appName + FILE_SEPARATOR + deployDate + "\n";
        sb.append("mkdir -p ").append(backupPath);
        sb.append("mv -f ").append(fileSavePath);
        sb.append(appName).append(" ").append(backupPath);
        logger.info("备份应用脚本:" + sb.toString());
        executeShellUtil.execute(sb.toString());
        //还原信息入库
        DeployHistory deployHistory = new DeployHistory();
        deployHistory.setAppName(appName);
        deployHistory.setDeployUser(SecurityUtils.getCurrentUsername());
        deployHistory.setIp(ip);
        deployHistory.setDeployId(id);
        deployHistoryService.save(deployHistory);
    }

    /**
     * 停App
     *
     * @param port             端口
     * @param executeShellUtil /
     */
    private void stopApp(int port, ExecuteShellUtils executeShellUtil) {
        //发送停止命令
        executeShellUtil.execute(String.format("lsof -i :%d|grep -v \"PID\"|awk '{print \"kill -9\",$2}'|sh", port));

    }

    /**
     * 指定端口程序是否在运行
     *
     * @param port             端口
     * @param executeShellUtil /
     * @return true 正在运行  false 已经停止
     */
    private boolean checkIsRunningStatus(int port, ExecuteShellUtils executeShellUtil) {
        String result = executeShellUtil.executeForResult(String.format("fuser -n tcp %d", port));
        return result.indexOf("/tcp:") > 0;
    }

    private ExecuteShellUtils getExecuteShellUtils(String ip) {
        ServerDTO ServerDTO = serverService.getByIp(ip);
        if (ServerDTO == null) {
            sendMsg("IP对应服务器信息不存在：" + ip, MsgType.ERROR);
            throw new BadRequestException("IP对应服务器信息不存在：" + ip);
        }
        return new ExecuteShellUtils(ip, ServerDTO.getAccount(), ServerDTO.getPassword(), ServerDTO.getPort());
    }

    private ScpClientUtils getScpClientUtils(String ip) {
        ServerDTO ServerDTO = serverService.getByIp(ip);
        if (ServerDTO == null) {
            sendMsg("IP对应服务器信息不存在：" + ip, MsgType.ERROR);
            throw new BadRequestException("IP对应服务器信息不存在：" + ip);
        }
        return ScpClientUtils.getInstance(ip, ServerDTO.getPort(), ServerDTO.getAccount(), ServerDTO.getPassword());
    }

    private void sendResultMsg(boolean result, StringBuilder sb) {
        if (result) {
            sb.append("<br>启动成功!");
            sendMsg(sb.toString(), MsgType.INFO);
        } else {
            sb.append("<br>启动失败!");
            sendMsg(sb.toString(), MsgType.ERROR);
        }
    }

    private void sendMsg(String msg, MsgType msgType) {
        try {
            WebSocketServer.sendInfo(new SocketMsg(msg, msgType), "deploy");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private boolean checkFile(ExecuteShellUtils executeShellUtil, AppDTO appDTO) {
        String result =
                executeShellUtil.executeForResult("find " + appDTO.getDeployPath() + " -name " + appDTO.getName());
        return result.indexOf(appDTO.getName()) > 0;
    }
}
