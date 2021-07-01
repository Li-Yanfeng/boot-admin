package org.utility.modules.system.service;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface RoleMenuService {

    /**
     * 根据 roleId 删除
     *
     * @param roleIds 角色Id集合
     */
    void removeByRoleIds(Collection<Long> roleIds);

    /**
     * 根据 menuId 删除
     *
     * @param menuIds 菜单Id集合
     */
    void removeByMenuIds(Collection<Long> menuIds);

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色Id
     * @return
     */
    List<Long> listMenuIdByRoleId(Long roleId);
}
