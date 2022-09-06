package com.boot.admin.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.mnt.mapper.ServerMapper;
import com.boot.admin.mnt.model.Server;
import com.boot.admin.mnt.service.ServerService;
import com.boot.admin.mnt.service.dto.ServerDTO;
import com.boot.admin.mnt.service.dto.ServerQuery;
import com.boot.admin.mnt.util.ExecuteShellUtils;
import com.boot.admin.util.Assert;
import com.boot.admin.util.ConvertUtils;
import com.boot.admin.util.FileUtils;
import com.boot.admin.util.QueryHelp;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 服务器 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements ServerService {

    @Override
    public void saveServer(Server resource) {
        baseMapper.insert(resource);
    }

    @Override
    public void removeServerByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateServerById(Server resource) {
        Long serverId = resource.getServerId();
        Assert.notNull(baseMapper.selectById(serverId));
        baseMapper.updateById(resource);
    }

    @Override
    public List<ServerDTO> listServers(ServerQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), ServerDTO.class);
    }

    @Override
    public Page<ServerDTO> listServers(ServerQuery query, Page<Server> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), ServerDTO.class);
    }

    @Override
    public ServerDTO getServerById(Long id) {
        Server server = baseMapper.selectById(id);
        Assert.notNull(server);
        return ConvertUtils.convert(server, ServerDTO.class);
    }

    @Override
    public ServerDTO getServerByIp(String ip) {
        return ConvertUtils.convert(lambdaQuery().eq(Server::getIp, ip).one(), ServerDTO.class);
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
    public void exportServer(List<ServerDTO> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(server -> {
            Map<String, Object> map = MapUtil.newHashMap(5, true);
            map.put("账号", server.getAccount());
            map.put("IP地址", server.getIp());
            map.put("名称", server.getName());
            map.put("密码", server.getPassword());
            map.put("端口", server.getPort());
            list.add(map);
        });
        FileUtils.downloadExcel("服务器", list, response);
    }
}
