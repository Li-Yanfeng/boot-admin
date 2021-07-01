package org.utility.modules.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.modules.mnt.mapper.DeployServerMapper;
import org.utility.modules.mnt.model.DeployServer;
import org.utility.modules.mnt.service.DeployServerService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用与服务器关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Service
public class DeployServerServiceImpl implements DeployServerService {

    private final DeployServerMapper deployServerMapper;

    public DeployServerServiceImpl(DeployServerMapper deployServerMapper) {
        this.deployServerMapper = deployServerMapper;
    }

    @Override
    public void removeByDeployIds(Collection<Long> deployIds) {
        if (CollUtil.isNotEmpty(deployIds)) {
            LambdaUpdateWrapper<DeployServer> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(DeployServer::getDeployId, deployIds);
            deployServerMapper.delete(wrapper);
        }
    }

    @Override
    public void removeByServerIds(Collection<Long> serverIds) {
        if (CollUtil.isNotEmpty(serverIds)) {
            LambdaUpdateWrapper<DeployServer> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(DeployServer::getServerId, serverIds);
            deployServerMapper.delete(wrapper);
        }
    }

    @Override
    public List<Long> listServerIdByDeployId(Long deployId) {
        if (deployId == null) {
            return null;
        }

        LambdaQueryWrapper<DeployServer> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DeployServer::getDeployId, deployId);
        List<DeployServer> deployServers = deployServerMapper.selectList(wrapper);
        return CollUtil.isNotEmpty(deployServers)
                ? deployServers.stream().map(DeployServer::getServerId).collect(Collectors.toList())
                : null;
    }
}
