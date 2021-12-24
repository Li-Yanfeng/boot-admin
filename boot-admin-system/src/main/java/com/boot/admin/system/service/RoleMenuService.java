package com.boot.admin.system.service;

import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.RoleMenu;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface RoleMenuService extends Service<RoleMenu> {

    /**
     * 根据 roleIds 删除
     *
     * @param roleIds 角色ID列表
     */
    void removeRoleMenuByRoleIds(Collection<Long> roleIds);

    /**
     * 根据 roleId 和 menuIds 删除
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    void removeRoleMenuByRoleIdEqAndMenuIdsIn(Long roleId, Collection<Long> menuIds);

    /**
     * 根据 menuIds 删除
     *
     * @param menuIds 菜单ID列表
     */
    void removeRoleMenuByMenuIds(Collection<Long> menuIds);

    /**
     * 根据 menuId 和 roleIds 删除
     *
     * @param menuId  菜单ID
     * @param roleIds 角色ID列表
     */
    void removeRoleMenuByMenuIdEqAndRoleIdsIn(Long menuId, Collection<Long> roleIds);

    /**
     * 根据 menuId 查询
     *
     * @param menuId 菜单ID
     * @return 列表查询结果
     */
    List<Long> listRoleIdsByMenuId(Long menuId);

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return 列表查询结果
     */
    List<Long> listMenuIdsByRoleId(Long roleId);
}
