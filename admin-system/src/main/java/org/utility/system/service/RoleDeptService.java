package org.utility.system.service;

import org.utility.core.service.Service;
import org.utility.system.model.RoleDept;

import java.util.Collection;
import java.util.List;

/**
 * 角色部门关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface RoleDeptService extends Service<RoleDept> {

    /**
     * 根据 roleIds 删除
     *
     * @param roleIds 角色ID列表
     */
    void removeRoleDeptByRoleIds(Collection<Long> roleIds);

    /**
     * 根据 roleId 和 deptIds 删除
     *
     * @param roleId  角色ID
     * @param deptIds 部门ID列表
     */
    void removeRoleDeptByRoleIdEqAndDeptIdsIn(Long roleId, Collection<Long> deptIds);

    /**
     * 根据 deptIds 删除
     *
     * @param deptIds 部门ID列表
     */
    void removeRoleDeptByDeptIds(Collection<Long> deptIds);

    /**
     * 根据 deptId 和 roleIds 删除
     *
     * @param deptId  部门ID
     * @param roleIds 角色ID列表
     */
    void removeRoleDeptByDeptIdEqAndRoleIdsIn(Long deptId, Collection<Long> roleIds);

    /**
     * 根据 deptId 查询
     *
     * @param deptId 部门ID
     * @return 列表查询结果
     */
    List<Long> listRoleIdsByDeptId(Long deptId);

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色ID
     * @return 列表查询结果
     */
    List<Long> listDeptIdsByRoleId(Long roleId);

    /**
     * 根据 deptIds 统计
     *
     * @param deptIds 部门ID列表
     * @return 列表查询结果
     */
    int countRoleIdByDeptIds(Collection<Long> deptIds);
}
