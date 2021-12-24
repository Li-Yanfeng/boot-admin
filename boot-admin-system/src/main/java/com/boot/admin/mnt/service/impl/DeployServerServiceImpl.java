package com.boot.admin.mnt.service.impl;

import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.mnt.mapper.DeployServerMapper;
import com.boot.admin.mnt.model.DeployServer;
import com.boot.admin.mnt.service.DeployServerService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部署服务器关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class DeployServerServiceImpl extends ServiceImpl<DeployServerMapper, DeployServer> implements DeployServerService {

    @Override
    public void removeDeployServerByDeployIds(Collection<Long> deployIds) {
        lambdaUpdate().in(DeployServer::getDeployId, deployIds).remove();
    }

    @Override
    public void removeDeployServerByDeployIdEqAndServerIdsIn(Long deployId, Collection<Long> serverIds) {
        lambdaUpdate().eq(DeployServer::getDeployId, deployId).in(DeployServer::getServerId, serverIds).remove();
    }

    @Override
    public void removeDeployServerByServerIds(Collection<Long> serverIds) {
        lambdaUpdate().in(DeployServer::getServerId, serverIds).remove();
    }

    @Override
    public void removeDeployServerByServerIdEqAndDeployIdsIn(Long serverId, Collection<Long> deployIds) {
        lambdaUpdate().eq(DeployServer::getServerId, serverId).in(DeployServer::getDeployId, deployIds).remove();
    }

    @Override
    public List<Long> listDeployIdsByServerId(Long serverId) {
        return lambdaQuery().eq(DeployServer::getServerId, serverId).list().stream().map(DeployServer::getDeployId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listServerIdsByDeployId(Long deployId) {
        return lambdaQuery().eq(DeployServer::getDeployId, deployId).list().stream().map(DeployServer::getServerId).collect(Collectors.toList());
    }
}
