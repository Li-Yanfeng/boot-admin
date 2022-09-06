package com.boot.admin.system.service.impl;

import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.system.mapper.RoleDeptMapper;
import com.boot.admin.system.model.RoleDept;
import com.boot.admin.system.service.RoleDeptService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色部门关联 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
public class RoleDeptServiceImpl extends ServiceImpl<RoleDeptMapper, RoleDept> implements RoleDeptService {

    @Override
    public void removeRoleDeptByRoleIds(Collection<Long> roleIds) {
        lambdaUpdate().in(RoleDept::getRoleId, roleIds).remove();
    }

    @Override
    public void removeRoleDeptByRoleIdEqAndDeptIdsIn(Long roleId, Collection<Long> deptIds) {
        lambdaUpdate().eq(RoleDept::getRoleId, roleId).in(RoleDept::getDeptId, deptIds).remove();
    }

    @Override
    public void removeRoleDeptByDeptIds(Collection<Long> deptIds) {
        lambdaUpdate().in(RoleDept::getDeptId, deptIds).remove();
    }

    @Override
    public void removeRoleDeptByDeptIdEqAndRoleIdsIn(Long deptId, Collection<Long> roleIds) {
        lambdaUpdate().eq(RoleDept::getDeptId, deptId).in(RoleDept::getRoleId, roleIds).remove();
    }

    @Override
    public List<Long> listRoleIdsByDeptId(Long deptId) {
        return lambdaQuery().eq(RoleDept::getDeptId, deptId).list().stream().map(RoleDept::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listDeptIdsByRoleId(Long roleId) {
        return lambdaQuery().eq(RoleDept::getRoleId, roleId).list().stream().map(RoleDept::getDeptId).collect(Collectors.toList());
    }

    @Override
    public int countRoleIdByDeptIds(Collection<Long> deptIds) {
        return lambdaQuery().in(RoleDept::getDeptId, deptIds).count();
    }
}
