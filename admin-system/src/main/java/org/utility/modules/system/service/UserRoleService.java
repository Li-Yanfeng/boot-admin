package org.utility.modules.system.service;

import java.util.Collection;

/**
 * 用户角色关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface UserRoleService {

    /**
     * 根据 userId 删除
     *
     * @param userIds 用户Id集合
     */
    void removeByUserIds(Collection<Long> userIds);

    /**
     * 根据 roleId 删除
     *
     * @param roleIds 角色Id集合
     */
    void removeByRoleIds(Collection<Long> roleIds);

    /**
     * 根据 roleId 查询总记录数
     *
     * @param roleIds 角色Id集合
     * @return /
     */
    Integer countByRoleIds(Collection<Long> roleIds);
}
