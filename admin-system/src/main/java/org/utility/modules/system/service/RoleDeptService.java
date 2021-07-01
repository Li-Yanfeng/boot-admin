package org.utility.modules.system.service;

import java.util.Collection;

/**
 * 角色部门关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface RoleDeptService {

    /**
     * 根据 roleId 删除
     *
     * @param roleIds 角色Id集合
     */
    void removeByRoleIds(Collection<Long> roleIds);

    /**
     * 根据 deptId 删除
     *
     * @param deptIds 部门Id集合
     */
    void removeByDeptIds(Collection<Long> deptIds);

    /**
     * 根据 deptId 查询总记录数
     *
     * @param deptIds 部门Id集合
     * @return /
     */
    Integer countByDeptIds(Collection<Long> deptIds);
}
