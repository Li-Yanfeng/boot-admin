package com.boot.admin.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mnt.mapper.DeployMapper;
import com.boot.admin.mnt.mapper.DeployServerMapper;
import com.boot.admin.mnt.mapper.ServerMapper;
import com.boot.admin.mnt.model.Deploy;
import com.boot.admin.mnt.model.DeployHistory;
import com.boot.admin.mnt.model.DeployServer;
import com.boot.admin.mnt.model.Server;
import com.boot.admin.mnt.service.*;
import com.boot.admin.mnt.service.dto.AppDTO;
import com.boot.admin.mnt.service.dto.DeployDTO;
import com.boot.admin.mnt.service.dto.DeployQuery;
import com.boot.admin.mnt.service.dto.ServerDTO;
import com.boot.admin.mnt.util.ExecuteShellUtils;
import com.boot.admin.mnt.util.ScpClientUtils;
import com.boot.admin.mnt.websocket.MsgType;
import com.boot.admin.mnt.websocket.SocketMsg;
import com.boot.admin.mnt.websocket.WebSocketServer;
import com.boot.admin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部署 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
public class DeployServiceImpl extends ServiceImpl<DeployMapper, Deploy> implements DeployService {

    private static final Logger logger = LoggerFactory.getLogger(DeployServiceImpl.class);

    /**
     * 循环次数
     */
    private final Integer count = 30;

    private final String FILE_SEPARATOR = "/";

    private final DeployMapper deployMapper;
    private final ServerMapper serverMapper;
    private final DeployServerMapper deployServerMapper;
    private final AppService appService;
    private final ServerService serverService;
    private final DeployServerService deployServerService;
    private final DeployHistoryService deployHistoryService;

    public DeployServiceImpl(DeployMapper deployMapper, ServerMapper serverMapper,
                             DeployServerMapper deployServerMapper, AppService appService,
                             ServerService serverService, DeployServerService deployServerService,
                             DeployHistoryService deployHistoryService) {
        this.deployMapper = deployMapper;
        this.serverMapper = serverMapper;
        this.deployServerMapper = deployServerMapper;
        this.appService = appService;
        this.serverService = serverService;
        this.deployServerService = deployServerService;
        this.deployHistoryService = deployHistoryService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDeploy(DeployDTO resource) {
        Deploy deploy = ConvertUtils.convert(resource, Deploy.class);
        deploy.setAppId(resource.getApp().getAppId());
        deployMapper.insert(deploy);

        if (CollUtil.isNotEmpty(resource.getDeploys())) {
            resource.getDeploys().forEach(server -> {
                DeployServer ds = new DeployServer();
                ds.setDeployId(deploy.getDeployId());
                ds.setServerId(server.getServerId());
                deployServerMapper.insert(ds);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeDeployByIds(Collection<Long> ids) {
        deployServerService.removeDeployServerByDeployIds(ids);
        baseMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDeployById(DeployDTO resource) {
        Long deployId = resource.getDeployId();
        Assert.notNull(baseMapper.selectById(deployId));

        Deploy deploy = ConvertUtils.convert(resource, Deploy.class);
        deploy.setAppId(resource.getApp().getAppId());
        deployMapper.updateById(deploy);

        List<ServerDTO> servers = resource.getDeploys();
        if (CollUtil.isNotEmpty(servers)) {
            // 更新数据
            List<Long> oldServerIds = deployServerService.listServerIdsByDeployId(deployId);
            List<Long> newServerIds = servers.stream().map(ServerDTO::getServerId).collect(Collectors.toList());
            List<Long> delServerIds = ComparatorUtils.findDataNotIncludedInSourceData(oldServerIds, newServerIds);
            List<Long> addServerIds = ComparatorUtils.findDataNotIncludedInSourceData(newServerIds, oldServerIds);
            if (CollUtil.isNotEmpty(delServerIds)) {
                deployServerService.removeDeployServerByDeployIdEqAndServerIdsIn(deployId, delServerIds);
            }
            if (CollUtil.isNotEmpty(addServerIds)) {
                addServerIds.forEach(serverId -> {
                    DeployServer ds = new DeployServer();
                    ds.setDeployId(deploy.getDeployId());
                    ds.setServerId(serverId);
                    deployServerMapper.insert(ds);
                });
            }
        }
    }

    @Override
    public List<DeployDTO> listDeploys(DeployQuery query) {
        List<DeployDTO> list = ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)),
            DeployDTO.class);
        for (DeployDTO dd : list) {
            dd.setApp(appService.getAppById(dd.getAppId()));

            LambdaQueryWrapper<Server> wrapper = Wrappers.lambdaQuery();
            wrapper.in(Server::getServerId, deployServerService.listServerIdsByDeployId(dd.getDeployId()));
            dd.setDeploys(ConvertUtils.convert(serverMapper.selectList(wrapper), ServerDTO.class));
        }
        return list;
    }

    @Override
    public Page<DeployDTO> listDeploys(DeployQuery query, Page<Deploy> page) {
        Page<DeployDTO> pageList = ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)),
            DeployDTO.class);
        for (DeployDTO dd : pageList.getRecords()) {
            dd.setApp(appService.getAppById(dd.getAppId()));

            LambdaQueryWrapper<Server> wrapper = Wrappers.lambdaQuery();
            wrapper.in(Server::getServerId, deployServerService.listServerIdsByDeployId(dd.getDeployId()));
            dd.setDeploys(ConvertUtils.convert(serverMapper.selectList(wrapper), ServerDTO.class));
        }
        return pageList;
    }

    @Override
    public DeployDTO getDeployById(Long id) {
        Deploy deploy = baseMapper.selectById(id);
        Assert.notNull(deploy);
        return ConvertUtils.convert(deploy, DeployDTO.class);
    }

    @Override
    public void deploy(String fileSavePath, Long appId) {
        deployApp(fileSavePath, appId);
    }

    @Override
    public String startServer(DeployDTO resource) {
        List<ServerDTO> deploys = resource.getDeploys();
        AppDTO app = resource.getApp();
        for (ServerDTO deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(deploy.getIp());
            // 为了防止重复启动，这里先停止应用
            stopApp(app.getPort(), executeShellUtil);
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            sendMsg("下发启动命令", MsgType.INFO);
            executeShellUtil.execute(app.getStartScript());
            sleep(3);
            sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看运行状态", MsgType.INFO);
            int i = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = checkIsRunningStatus(app.getPort(), executeShellUtil);
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
        List<ServerDTO> deploys = resource.getDeploys();
        AppDTO app = resource.getApp();
        for (ServerDTO deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(deploy.getIp());
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            sendMsg("下发停止命令", MsgType.INFO);
            // 停止应用
            stopApp(app.getPort(), executeShellUtil);
            sleep(1);
            boolean result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>关闭失败!");
                sendMsg(sb.toString(), MsgType.ERROR);
            } else {
                sb.append("<br>关闭成功!");
                sendMsg(sb.toString(), MsgType.INFO);
            }
            logger.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    @Override
    public String serverStatus(DeployDTO resource) {
        List<ServerDTO> Servers = resource.getDeploys();
        AppDTO app = resource.getApp();
        for (ServerDTO Server : Servers) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(Server.getIp());
            sb.append("服务器:").append(Server.getName()).append("<br>应用:").append(app.getName());
            boolean result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>正在运行");
                sendMsg(sb.toString(), MsgType.INFO);
            } else {
                sb.append("<br>已停止!");
                sendMsg(sb.toString(), MsgType.ERROR);
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
        String deployDate = DateUtil.format(resource.getCreateTime(), DatePattern.PURE_DATETIME_PATTERN);
        AppDTO app = appService.getAppById(deployInfo.getAppId());
        if (app == null) {
            sendMsg("应用信息不存在：" + resource.getAppName(), MsgType.ERROR);
            throw new BadRequestException("应用信息不存在：" + resource.getAppName());
        }
        String backupPath = app.getBackupPath() + FILE_SEPARATOR;
        backupPath += resource.getAppName() + FILE_SEPARATOR + deployDate;
        // 这个是服务器部署路径
        String deployPath = app.getDeployPath();
        String ip = resource.getIp();
        ExecuteShellUtils executeShellUtil = getExecuteShellUtils(ip);
        String msg;

        msg = String.format("登陆到服务器:%s", ip);
        logger.info(msg);
        sendMsg(msg, MsgType.INFO);
        sendMsg("停止原来应用", MsgType.INFO);
        // 停止应用
        stopApp(app.getPort(), executeShellUtil);
        // 删除原来应用
        sendMsg("删除应用", MsgType.INFO);
        executeShellUtil.execute("rm -rf " + deployPath + FILE_SEPARATOR + resource.getAppName());
        // 还原应用
        sendMsg("还原应用", MsgType.INFO);
        executeShellUtil.execute("cp -r " + backupPath + "/. " + deployPath);
        sendMsg("启动应用", MsgType.INFO);
        executeShellUtil.execute(app.getStartScript());
        sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看启动状态", MsgType.INFO);
        int i = 0;
        boolean result = false;
        // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
        while (i++ < count) {
            result = checkIsRunningStatus(app.getPort(), executeShellUtil);
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
    public void exportDeploy(List<DeployDTO> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(deploy -> {
            Map<String, Object> map = MapUtil.newHashMap(3, true);
            map.put("应用名称", deploy.getApp().getName());
            map.put("服务器", deploy.getServers());
            map.put("部署日期", deploy.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel("部署", list, response);
    }

    /**
     * @param fileSavePath 本机路径
     * @param id           ID
     */
    private void deployApp(String fileSavePath, Long id) {
        DeployDTO deploy = getDeployById(id);
        if (deploy == null) {
            sendMsg("部署信息不存在", MsgType.ERROR);
            throw new BadRequestException("部署信息不存在");
        }
        AppDTO app = deploy.getApp();
        if (app == null) {
            sendMsg("包对应应用信息不存在", MsgType.ERROR);
            throw new BadRequestException("包对应应用信息不存在");
        }
        int port = app.getPort();
        // 这个是服务器部署路径
        String uploadPath = app.getUploadPath();
        StringBuilder sb = new StringBuilder();
        String msg;
        List<ServerDTO> deploys = deploy.getDeploys();
        for (ServerDTO deployDTO : deploys) {
            String ip = deployDTO.getIp();
            ExecuteShellUtils executeShellUtil = getExecuteShellUtils(ip);
            // 判断是否第一次部署
            boolean flag = checkFile(executeShellUtil, app);
            // 第一步要确认服务器上有这个目录
            executeShellUtil.execute("mkdir -p " + app.getUploadPath());
            executeShellUtil.execute("mkdir -p " + app.getBackupPath());
            executeShellUtil.execute("mkdir -p " + app.getDeployPath());
            // 上传文件
            msg = String.format("登陆到服务器:%s", ip);
            ScpClientUtils scpClientUtil = getScpClientUtils(ip);
            logger.info(msg);
            sendMsg(msg, MsgType.INFO);
            msg = String.format("上传文件到服务器:%s<br>目录:%s下，请稍等...", ip, uploadPath);
            sendMsg(msg, MsgType.INFO);
            scpClientUtil.putFile(fileSavePath, uploadPath);
            if (flag) {
                sendMsg("停止原来应用", MsgType.INFO);
                // 停止应用
                stopApp(port, executeShellUtil);
                sendMsg("备份原来应用", MsgType.INFO);
                // 备份应用
                backupApp(executeShellUtil, ip, app.getDeployPath() + FILE_SEPARATOR, app.getName(),
                    app.getBackupPath() + FILE_SEPARATOR, id);
            }
            sendMsg("部署应用", MsgType.INFO);
            // 部署文件,并启动应用
            String deployScript = app.getDeployScript();
            executeShellUtil.execute(deployScript);
            sleep(3);
            sendMsg("应用部署中，请耐心等待部署结果，或者稍后手动查看部署状态", MsgType.INFO);
            int i = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = checkIsRunningStatus(port, executeShellUtil);
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
        String deployDate = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder sb = new StringBuilder();
        backupPath += appName + FILE_SEPARATOR + deployDate + "\n";
        sb.append("mkdir -p ").append(backupPath);
        sb.append("mv -f ").append(fileSavePath);
        sb.append(appName).append(" ").append(backupPath);
        logger.info("备份应用脚本:" + sb);
        executeShellUtil.execute(sb.toString());
        // 还原信息入库
        DeployHistory deployHistory = new DeployHistory();
        deployHistory.setAppName(appName);
        deployHistory.setIp(ip);
        deployHistory.setDeployId(id);
        deployHistoryService.saveDeployHistory(deployHistory);
    }

    /**
     * 停App
     *
     * @param port             端口
     * @param executeShellUtil /
     */
    private void stopApp(int port, ExecuteShellUtils executeShellUtil) {
        // 发送停止命令
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
        ServerDTO ServerDTO = serverService.getServerByIp(ip);
        if (ServerDTO == null) {
            sendMsg("IP对应服务器信息不存在：" + ip, MsgType.ERROR);
            throw new BadRequestException("IP对应服务器信息不存在：" + ip);
        }
        return new ExecuteShellUtils(ip, ServerDTO.getAccount(), ServerDTO.getPassword(), ServerDTO.getPort());
    }

    private ScpClientUtils getScpClientUtils(String ip) {
        ServerDTO ServerDTO = serverService.getServerByIp(ip);
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
