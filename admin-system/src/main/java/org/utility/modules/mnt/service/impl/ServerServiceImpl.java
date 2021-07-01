package org.utility.modules.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.base.impl.ServiceImpl;
import org.utility.modules.mnt.mapper.ServerMapper;
import org.utility.modules.mnt.model.Server;
import org.utility.modules.mnt.service.ServerService;
import org.utility.modules.mnt.service.dto.ServerDTO;
import org.utility.modules.mnt.service.dto.ServerQuery;
import org.utility.modules.mnt.util.ExecuteShellUtils;
import org.utility.util.ConvertUtils;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 服务器管理 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, ServerDTO, ServerQuery, Server> implements ServerService {

    private final ServerMapper serverMapper;

    public ServerServiceImpl(ServerMapper serverMapper) {
        this.serverMapper = serverMapper;
    }

    @Override
    public ServerDTO getByIp(String ip) {
        LambdaQueryWrapper<Server> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Server::getIp, ip);
        Server deploy = serverMapper.selectOne(wrapper);
        return ConvertUtils.convert(deploy, ServerDTO.class);
    }

    @Override
    public Boolean testConnect(Server resource) {
        ExecuteShellUtils executeShellUtil = null;
        try {
            executeShellUtil = new ExecuteShellUtils(resource.getIp(), resource.getAccount(), resource.getPassword(),
                    resource.getPort());
            return executeShellUtil.execute("ls") == 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (executeShellUtil != null) {
                executeShellUtil.close();
            }
        }
    }

    @Override
    public void download(HttpServletResponse response, List<ServerDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (ServerDTO server : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(5, true);
            map.put("账号", server.getAccount());
            map.put("IP地址", server.getIp());
            map.put("名称", server.getName());
            map.put("密码", server.getPassword());
            map.put("端口", server.getPort());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
