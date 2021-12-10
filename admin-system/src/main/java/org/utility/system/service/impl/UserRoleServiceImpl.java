package org.utility.system.service.impl;

import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.system.mapper.UserRoleMapper;
import org.utility.system.model.UserRole;
import org.utility.system.service.UserRoleService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public void removeUserRoleByUserIds(Collection<Long> userIds) {
        lambdaUpdate().in(UserRole::getUserId, userIds).remove();
    }

    @Override
    public void removeUserRoleByUserIdEqAndRoleIdsIn(Long userId, Collection<Long> roleIds) {
        lambdaUpdate().eq(UserRole::getUserId, userId).in(UserRole::getRoleId, roleIds).remove();
    }

    @Override
    public void removeUserRoleByRoleIds(Collection<Long> roleIds) {
        lambdaUpdate().in(UserRole::getRoleId, roleIds).remove();
    }

    @Override
    public void removeUserRoleByRoleIdEqAndUserIdsIn(Long roleId, Collection<Long> userIds) {
        lambdaUpdate().eq(UserRole::getRoleId, roleId).in(UserRole::getUserId, userIds).remove();
    }

    @Override
    public List<Long> listUserIdsByRoleId(Long roleId) {
        return lambdaQuery().eq(UserRole::getRoleId, roleId).list().stream().map(UserRole::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listRoleIdsByUserId(Long userId) {
        return lambdaQuery().eq(UserRole::getUserId, userId).list().stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public int countUserIdByRoleIds(Collection<Long> roleIds) {
        return lambdaQuery().in(UserRole::getRoleId, roleIds).count();
    }
}
