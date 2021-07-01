package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.constant.SystemConfigConsts;
import org.utility.modules.system.mapper.RoleDeptMapper;
import org.utility.modules.system.model.RoleDept;
import org.utility.modules.system.service.RoleDeptService;

import java.util.Collection;

/**
 * 角色部门关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
public class RoleDeptServiceImpl implements RoleDeptService {

    private final RoleDeptMapper roleDeptMapper;

    public RoleDeptServiceImpl(RoleDeptMapper roleDeptMapper) {
        this.roleDeptMapper = roleDeptMapper;
    }

    @Override
    public void removeByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaUpdateWrapper<RoleDept> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(RoleDept::getRoleId, roleIds);
            roleDeptMapper.delete(wrapper);
        }
    }

    @Override
    public void removeByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isNotEmpty(deptIds)) {
            LambdaUpdateWrapper<RoleDept> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(RoleDept::getDeptId, deptIds);
            roleDeptMapper.delete(wrapper);
        }
    }

    @Override
    public Integer countByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isNotEmpty(deptIds)) {
            LambdaQueryWrapper<RoleDept> wrapper = Wrappers.lambdaQuery();
            wrapper.in(RoleDept::getDeptId, deptIds);
            return roleDeptMapper.selectCount(wrapper);
        } else {
            return SystemConfigConsts.NO_RECORD;
        }
    }
}
