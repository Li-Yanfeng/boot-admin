package org.utility.mnt.service.impl;

import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.mnt.mapper.DeployServerMapper;
import org.utility.mnt.model.DeployServer;
import org.utility.mnt.service.DeployServerService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务器 服务实现类
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
    public void removeDeployServerByServerIds(Collection<Long> serverIds) {
        lambdaUpdate().in(DeployServer::getServerId, serverIds).remove();
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
