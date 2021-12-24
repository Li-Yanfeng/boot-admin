package com.boot.admin.mnt.service;

import com.boot.admin.core.service.Service;
import com.boot.admin.mnt.model.DeployServer;

import java.util.Collection;
import java.util.List;

/**
 * 部署服务器关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface DeployServerService extends Service<DeployServer> {

    /**
     * 根据 deployIds 删除
     *
     * @param deployIds 部署ID列表
     */
    void removeDeployServerByDeployIds(Collection<Long> deployIds);

    /**
     * 根据 deployId 和 serverIds 删除
     *
     * @param deployId  部署ID
     * @param serverIds 服务器ID列表
     */
    void removeDeployServerByDeployIdEqAndServerIdsIn(Long deployId, Collection<Long> serverIds);

    /**
     * 根据 serverIds 删除
     *
     * @param serverIds 服务器ID列表
     */
    void removeDeployServerByServerIds(Collection<Long> serverIds);

    /**
     * 根据 serverId 和 deployIds 删除
     *
     * @param serverId  服务器ID
     * @param deployIds 部署ID列表
     */
    void removeDeployServerByServerIdEqAndDeployIdsIn(Long serverId, Collection<Long> deployIds);

    /**
     * 根据 serverId 查询
     *
     * @param serverId 服务器ID
     * @return 列表查询结果
     */
    List<Long> listDeployIdsByServerId(Long serverId);

    /**
     * 根据 deployId 查询
     *
     * @param deployId 部署ID
     * @return 列表查询结果
     */
    List<Long> listServerIdsByDeployId(Long deployId);
}
