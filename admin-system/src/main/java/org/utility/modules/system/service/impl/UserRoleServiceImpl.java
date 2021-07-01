package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.constant.SystemConfigConsts;
import org.utility.modules.system.mapper.UserRoleMapper;
import org.utility.modules.system.model.UserRole;
import org.utility.modules.system.service.UserRoleService;

import java.util.Collection;

/**
 * 用户角色关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public void removeByUserIds(Collection<Long> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            LambdaUpdateWrapper<UserRole> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(UserRole::getUserId, userIds);
            userRoleMapper.delete(wrapper);
        }
    }

    @Override
    public void removeByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaUpdateWrapper<UserRole> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(UserRole::getRoleId, roleIds);
            userRoleMapper.delete(wrapper);
        }
    }

    @Override
    public Integer countByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
            wrapper.in(UserRole::getRoleId, roleIds);
            return userRoleMapper.selectCount(wrapper);
        } else {
            return SystemConfigConsts.NO_RECORD;
        }
    }
}
