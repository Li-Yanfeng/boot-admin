package org.utility.mnt.service;

import org.utility.core.service.Service;
import org.utility.mnt.model.DeployServer;

import java.util.Collection;
import java.util.List;

/**
 * 服务器 服务类
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
     * 根据 serverIds 删除
     *
     * @param serverIds 服务ID列表
     */
    void removeDeployServerByServerIds(Collection<Long> serverIds);

    /**
     * 根据 serverId 查询
     *
     * @param serverId 服务ID
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
