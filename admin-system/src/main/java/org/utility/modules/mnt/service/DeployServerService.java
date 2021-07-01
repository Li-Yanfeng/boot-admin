package org.utility.modules.mnt.service;

import java.util.Collection;
import java.util.List;

/**
 * 应用与服务器关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public interface DeployServerService {

    /**
     * 根据 deployId 删除
     *
     * @param deployIds 部署Id集合
     */
    void removeByDeployIds(Collection<Long> deployIds);

    /**
     * 根据 serverId 删除
     *
     * @param serverIds 服务器Id集合
     */
    void removeByServerIds(Collection<Long> serverIds);

    /**
     * 根据 roleId 查询
     *
     * @param deployId 部署Id
     * @return /
     */
    List<Long> listServerIdByDeployId(Long deployId);
}
