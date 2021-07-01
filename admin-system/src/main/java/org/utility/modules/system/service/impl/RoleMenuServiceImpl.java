package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.modules.system.mapper.RoleMenuMapper;
import org.utility.modules.system.model.RoleMenu;
import org.utility.modules.system.service.RoleMenuService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    private final RoleMenuMapper roleMenuMapper;

    public RoleMenuServiceImpl(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public void removeByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaUpdateWrapper<RoleMenu> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(RoleMenu::getRoleId, roleIds);
            roleMenuMapper.delete(wrapper);
        }
    }

    @Override
    public void removeByMenuIds(Collection<Long> menuIds) {
        if (CollUtil.isNotEmpty(menuIds)) {
            LambdaUpdateWrapper<RoleMenu> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(RoleMenu::getMenuId, menuIds);
            roleMenuMapper.delete(wrapper);
        }
    }


    @Override
    public List<Long> listMenuIdByRoleId(Long roleId) {
        if (roleId == null) {
            return null;
        }

        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(wrapper);
        return CollUtil.isNotEmpty(roleMenus)
                ? roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList())
                : null;
    }
}
