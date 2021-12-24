package com.boot.admin.system.service;

import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.UserRole;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface UserRoleService extends Service<UserRole> {

    /**
     * 根据 userIds 删除
     *
     * @param userIds 用户ID列表
     */
    void removeUserRoleByUserIds(Collection<Long> userIds);

    /**
     * 根据 userId 和 roleIds 删除
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    void removeUserRoleByUserIdEqAndRoleIdsIn(Long userId, Collection<Long> roleIds);

    /**
     * 根据 roleIds 删除
     *
     * @param roleIds 角色ID列表
     */
    void removeUserRoleByRoleIds(Collection<Long> roleIds);

    /**
     * 根据 roleId 和 userIds 删除
     *
     * @param roleId  角色ID
     * @param userIds 用户ID列表
     */
    void removeUserRoleByRoleIdEqAndUserIdsIn(Long roleId, Collection<Long> userIds);

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return 列表查询结果
     */
    List<Long> listUserIdsByRoleId(Long roleId);

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return 列表查询结果
     */
    List<Long> listRoleIdsByUserId(Long userId);

    /**
     * 根据 roleIds 统计
     *
     * @param roleIds 角色ID列表
     * @return 列表查询结果
     */
    int countUserIdByRoleIds(Collection<Long> roleIds);
}
