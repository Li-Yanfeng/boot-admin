package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.base.impl.ServiceImpl;
import org.utility.constant.CacheConsts;
import org.utility.exception.BadRequestException;
import org.utility.modules.system.mapper.DeptMapper;
import org.utility.modules.system.mapper.UserMapper;
import org.utility.modules.system.model.Dept;
import org.utility.modules.system.model.User;
import org.utility.modules.system.service.DeptService;
import org.utility.modules.system.service.RoleDeptService;
import org.utility.modules.system.service.dto.DeptDTO;
import org.utility.modules.system.service.dto.DeptQuery;
import org.utility.util.*;
import org.utility.util.enums.DataScopeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
@CacheConfig(cacheNames = {"dept"})
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptDTO, DeptQuery, Dept> implements DeptService {

    private final DeptMapper deptMapper;
    private final UserMapper userMapper;
    private final RoleDeptService roleDeptService;

    private final RedisUtils redisUtils;

    public DeptServiceImpl(DeptMapper deptMapper, UserMapper userMapper, RoleDeptService roleDeptService,
                           RedisUtils redisUtils) {
        this.deptMapper = deptMapper;
        this.userMapper = userMapper;
        this.roleDeptService = roleDeptService;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Dept resource) {
        deptMapper.insert(resource);
        // 计算子节点数目
        resource.setSubCount(0);
        if (resource.getPid() != null) {
            // 更新子节点
            this.updateSubCnt(resource.getPid());
            // 清理缓存
            this.delCaches(resource.getDeptId(), null, resource.getPid());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Collection<Long> ids) {
        for (Long id : ids) {
            DeptDTO dept = this.getById(id);
            // 清理缓存
            this.delCaches(id, dept.getPid(), null);
            // 更新子节点
            this.updateSubCnt(dept.getPid());
        }
        roleDeptService.removeByDeptIds(ids);
        deptMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateById(Dept resource) {
        Long deptId = resource.getDeptId();
        // 旧的部门
        Long oldPid = this.getById(deptId).getPid();
        if (resource.getPid() != null && deptId.equals(resource.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        deptMapper.updateById(resource);
        this.updateSubCnt(oldPid);
        this.updateSubCnt(resource.getPid());
        // 清理缓存
        this.delCaches(deptId, oldPid, resource.getPid());
    }

    @Override
    public List<DeptDTO> list(DeptQuery query) {
        String dataScopeType = SecurityUtils.getDataScopeType();
        if (dataScopeType.equals(DataScopeEnum.ALL.getValue())) {
            query.setPidIsNull(true);
        }
        return ConvertUtils.convertList(deptMapper.selectList(QueryHelp.queryWrapper(query)), DeptDTO.class);
    }

    @Override
    public List<Dept> listByPid(Long pid) {
        LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Dept::getPid, pid);
        return deptMapper.selectList(wrapper);
    }

    @Override
    public Set<DeptDTO> listByRoleId(Long roleId) {
        return ConvertUtils.convertSet(deptMapper.selectByRoleId(roleId), DeptDTO.class);
    }

    @Override
    public List<Long> listDeptChildren(Long deptId, List<Dept> deptList) {
        List<Long> list = CollUtil.newArrayList();
        deptList.forEach(dept -> {
            if (dept != null && dept.getEnabled()) {
                LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(Dept::getPid, dept.getDeptId());
                List<Dept> depts = deptMapper.selectList(wrapper);
                if (depts.size() != 0) {
                    list.addAll(listDeptChildren(dept.getDeptId(), depts));
                }
                list.add(dept.getDeptId());
            }
        });
        return list;
    }

    @Override
    public Set<Long> listDeleteDept(List<Dept> deptList, Set<Long> deptIds) {
        for (Dept dept : deptList) {
            deptIds.add(dept.getDeptId());
            List<Dept> depts = this.listByPid(dept.getDeptId());
            if (CollUtil.isNotEmpty(depts)) {
                listDeleteDept(depts, deptIds);
            }
        }
        return deptIds;
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public DeptDTO getById(Long id) {
        return super.getById(id);
    }

    @Override
    public List<DeptDTO> getSuperior(DeptDTO deptDto, List<Dept> depts) {
        LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery();
        if (deptDto.getPid() == null) {
            wrapper.isNull(Dept::getPid);
            depts.addAll(deptMapper.selectList(wrapper));
            return ConvertUtils.convertList(depts, DeptDTO.class);
        }
        wrapper.eq(Dept::getPid, deptDto.getPid());
        depts.addAll(deptMapper.selectList(wrapper));
        return getSuperior(ConvertUtils.convert(getById(deptDto.getPid()), DeptDTO.class), depts);
    }

    @Override
    public Map<String, Object> buildTree(List<DeptDTO> deptDtos) {
        Set<DeptDTO> trees = CollUtil.newLinkedHashSet();
        Set<DeptDTO> depts = CollUtil.newLinkedHashSet();
        List<String> deptNames = deptDtos.stream().map(DeptDTO::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDTO deptDTO : deptDtos) {
            isChild = false;
            if (deptDTO.getPid() == null) {
                trees.add(deptDTO);
            }
            for (DeptDTO it : deptDtos) {
                if (it.getPid() != null && it.getPid().equals(deptDTO.getDeptId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(CollUtil.newArrayList());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                depts.add(deptDTO);
            } else {
                DeptDTO dept = null;
                if (null != deptDTO.getPid()) {
                    dept = this.getById(deptDTO.getPid());
                }
                if (null != dept && !deptNames.contains(dept.getName())) {
                    depts.add(deptDTO);
                }
            }
        }

        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("totalElements", deptDtos.size());
        map.put("content", CollUtil.isEmpty(trees) ? deptDtos : trees);
        return map;
    }

    @Override
    public void verification(Set<Long> deptIds) {
        if (userMapper.countByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if (roleDeptService.countByDeptIds(deptIds) > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    @Override
    public void download(HttpServletResponse response, List<DeptDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (DeptDTO dept : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(3, true);
            map.put("部门名称", dept.getName());
            map.put("部门状态", dept.getEnabled() ? "启用" : "停用");
            map.put("创建日期", dept.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 更新子节点
     *
     * @param deptId 部门Id
     */
    private void updateSubCnt(Long deptId) {
        if (deptId == null) {
            return;
        }
        LambdaQueryWrapper<Dept> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Dept::getPid, deptId);
        int count = deptMapper.selectCount(queryWrapper);

        LambdaUpdateWrapper<Dept> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Dept::getDeptId, deptId);
        Dept dept = new Dept();
        dept.setSubCount(count);
        deptMapper.update(dept, updateWrapper);
    }

    /**
     * 重复数据删除
     *
     * @param list 部门集合
     * @return /
     */
    private List<DeptDTO> deduplication(List<DeptDTO> list) {
        List<DeptDTO> deptDtos = new ArrayList<>();
        for (DeptDTO deptDto : list) {
            boolean flag = true;
            for (DeptDTO dto : list) {
                if (dto.getDeptId().equals(deptDto.getPid())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                deptDtos.add(deptDto);
            }
        }
        return deptDtos;
    }

    /**
     * 清理缓存
     *
     * @param deptId 部门Id
     */
    public void delCaches(Long deptId, Long pidOld, Long pidNew) {
        List<User> users = userMapper.selectByDeptId(deptId);
        // 删除数据权限
        redisUtils.delByKeys(CacheConsts.DATA_USER, users.stream().map(User::getUserId).collect(Collectors.toSet()));
        redisUtils.del(CacheConsts.DEPT_ID + deptId);
        if (pidOld != null) {
            redisUtils.del(CacheConsts.DEPT_PID + pidOld);
        }
        if (pidNew != null) {
            redisUtils.del(CacheConsts.DEPT_PID + pidNew);
        }
    }
}
