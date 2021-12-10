package org.utility.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.utility.system.service.DataService;
import org.utility.system.service.DeptService;
import org.utility.system.service.RoleService;
import org.utility.system.service.dto.DeptDTO;
import org.utility.system.service.dto.DeptQuery;
import org.utility.system.service.dto.RoleSmallDTO;
import org.utility.system.service.dto.UserDTO;
import org.utility.util.enums.DataScopeEnum;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
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
                case THIS_LEVEL:
                    deptIds.add(user.getDept().getDeptId());
                    break;
                case CUSTOMIZE:
                    deptIds.addAll(getCustomize(deptIds, role));
                    break;
                default:
                    return CollUtil.newArrayList(deptIds);
            }
        }
        return CollUtil.newArrayList(deptIds);
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
            deptIds.add(dept.getDeptId());
            List<DeptDTO> deptChildren = deptService.listDepts(new DeptQuery(dept.getDeptId()));
            if (CollUtil.isNotEmpty(deptChildren)) {
                List<DeptDTO> deptDTOS = deptService.listDeptsChildren(CollUtil.newArrayList(dept), deptChildren);
                deptIds.addAll(deptDTOS.stream().map(DeptDTO::getDeptId).collect(Collectors.toList()));
            }
        }
        return deptIds;
    }
}
