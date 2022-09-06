package com.boot.admin.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.boot.admin.system.service.DataService;
import com.boot.admin.system.service.DeptService;
import com.boot.admin.system.service.RoleService;
import com.boot.admin.system.service.dto.DeptDTO;
import com.boot.admin.system.service.dto.RoleSmallDTO;
import com.boot.admin.system.service.dto.UserDTO;
import com.boot.admin.util.enums.DataScopeEnum;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"data"})
public class DataServiceImpl implements DataService {

    private final RoleService roleService;
    private final DeptService deptService;

    public DataServiceImpl(RoleService roleService, DeptService deptService) {
        this.roleService = roleService;
        this.deptService = deptService;
    }

    @Cacheable(key = "'user:' + #p0.userId")
    @Override
    public List<Long> listDeptIds(UserDTO user) {
        // 用于存储部门id
        Set<Long> deptIds = CollUtil.newHashSet();
        // 查询用户角色
        List<RoleSmallDTO> roleSet = roleService.listRolesByUserId(user.getUserId());
        // 获取对应的部门ID
        for (RoleSmallDTO role : roleSet) {
            DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case ORG:
                    deptIds.add(user.getDept().getDeptId());
                    break;
                case ORG_AND_CHILD:
                    deptIds.addAll(listDeptsChildren(user.getDept().getDeptId()));
                    break;
                case CUSTOM:
                    deptIds.addAll(getCustomize(deptIds, role));
                    break;
                default:
                    break;
            }
        }
        return CollUtil.newArrayList(deptIds);
    }


    /**
     * 获取所有子节点 (包含当前节点)
     *
     * @param deptId 部门ID
     * @return 数据权限ID
     */
    private Set<Long> listDeptsChildren(Long deptId) {
        List<DeptDTO> depts = deptService.listDeptsChildren(deptId);
        return depts.stream().map(DeptDTO::getDeptId).collect(Collectors.toSet());
    }

    /**
     * 获取自定义的数据权限
     *
     * @param deptIds 部门ID
     * @param role    角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(Set<Long> deptIds, RoleSmallDTO role) {
        List<DeptDTO> depts = deptService.listDeptsByRoleId(role.getRoleId());
        for (DeptDTO dept : depts) {
            List<DeptDTO> deptDTOS = deptService.listDeptsChildren(dept.getDeptId());
            deptIds.addAll(deptDTOS.stream().map(DeptDTO::getDeptId).collect(Collectors.toList()));
        }
        return deptIds;
    }
}
