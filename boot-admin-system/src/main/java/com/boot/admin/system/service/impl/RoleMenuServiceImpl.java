package com.boot.admin.system.service.impl;

import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.system.mapper.RoleMenuMapper;
import com.boot.admin.system.model.RoleMenu;
import com.boot.admin.system.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单关联 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public void removeRoleMenuByRoleIds(Collection<Long> roleIds) {
        lambdaUpdate().in(RoleMenu::getRoleId, roleIds).remove();
    }

    @Override
    public void removeRoleMenuByRoleIdEqAndMenuIdsIn(Long roleId, Collection<Long> menuIds) {
        lambdaUpdate().eq(RoleMenu::getRoleId, roleId).in(RoleMenu::getMenuId, menuIds).remove();
    }

    @Override
    public void removeRoleMenuByMenuIds(Collection<Long> menuIds) {
        lambdaUpdate().in(RoleMenu::getMenuId, menuIds).remove();
    }

    @Override
    public void removeRoleMenuByMenuIdEqAndRoleIdsIn(Long menuId, Collection<Long> roleIds) {
        lambdaUpdate().eq(RoleMenu::getMenuId, menuId).in(RoleMenu::getRoleId, roleIds).remove();
    }

    @Override
    public List<Long> listRoleIdsByMenuId(Long menuId) {
        return lambdaQuery().eq(RoleMenu::getMenuId, menuId).list().stream().map(RoleMenu::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listMenuIdsByRoleId(Long roleId) {
        return lambdaQuery().eq(RoleMenu::getRoleId, roleId).list().stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }
}
