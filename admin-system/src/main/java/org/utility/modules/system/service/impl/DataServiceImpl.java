package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.utility.modules.system.model.Dept;
import org.utility.modules.system.service.DataService;
import org.utility.modules.system.service.DeptService;
import org.utility.modules.system.service.RoleService;
import org.utility.modules.system.service.dto.DeptDTO;
import org.utility.modules.system.service.dto.RoleSmallDTO;
import org.utility.modules.system.service.dto.UserDTO;
import org.utility.util.enums.DataScopeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @Override
    @Cacheable(key = "'user:' + #p0.userId")
    public List<Long> getDeptIds(UserDTO user) {
        // 用于存储部门id
        Set<Long> deptIds = CollUtil.newHashSet();
        // 查询用户角色
        List<RoleSmallDTO> roleSet = roleService.listByUsersId(user.getUserId());
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
                    return new ArrayList<>(deptIds);
            }
        }
        return new ArrayList<>(deptIds);
    }

    /**
     * 获取自定义的数据权限
     *
     * @param deptIds 部门ID
     * @param role    角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(Set<Long> deptIds, RoleSmallDTO role) {
        Set<DeptDTO> depts = deptService.listByRoleId(role.getRoleId());
        for (DeptDTO dept : depts) {
            deptIds.add(dept.getDeptId());
            List<Dept> deptChildren = deptService.listByPid(dept.getDeptId());
            if (deptChildren != null && deptChildren.size() != 0) {
                deptIds.addAll(deptService.listDeptChildren(dept.getDeptId(), deptChildren));
            }
        }
        return deptIds;
    }
}
