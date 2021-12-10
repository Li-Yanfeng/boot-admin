package org.utility.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.constant.CacheKey;
import org.utility.constant.SystemConstant;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.system.mapper.DeptMapper;
import org.utility.system.mapper.UserMapper;
import org.utility.system.model.Dept;
import org.utility.system.model.User;
import org.utility.system.service.DeptService;
import org.utility.system.service.RoleDeptService;
import org.utility.system.service.dto.DeptDTO;
import org.utility.system.service.dto.DeptQuery;
import org.utility.util.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"dept"})
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    private final UserMapper userMapper;
    private final RoleDeptService roleDeptService;

    private final RedisUtils redisUtils;

    public DeptServiceImpl(UserMapper userMapper, RoleDeptService roleDeptService, RedisUtils redisUtils) {
        this.userMapper = userMapper;
        this.roleDeptService = roleDeptService;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDept(Dept resource) {
        // 计算子节点数目
        resource.setSubCount(0);
        baseMapper.insert(resource);
        // 更新节点
        updateSubCnt(resource.getPid());
        // 清理缓存
        delCaches(null, null, resource.getPid());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeDept(Collection<DeptDTO> resources) {
        for (DeptDTO resource : resources) {
            // 清理缓存
            delCaches(resource.getDeptId(), resource.getPid(), null);
            // 删除当前节点
            baseMapper.deleteById(resource.getDeptId());
            // 更新节点
            updateSubCnt(resource.getPid());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDeptById(Dept resource) {
        Long deptId = resource.getDeptId();
        Dept dept = baseMapper.selectById(deptId);
        ValidationUtils.notNull(dept, "Dept", "deptId", deptId);

        // 旧部门
        Long oldPid = dept.getPid();
        // 新部门
        Long newPid = resource.getPid();
        if (ObjectUtil.isNotNull(newPid) && deptId.equals(newPid)) {
            throw new BadRequestException("上级不能为自己");
        }
        baseMapper.updateById(resource);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
        delCaches(deptId, oldPid, newPid);
    }

    @Cacheable(key = "'pid:' + #p0.pid", condition = "#p0.pid != null")
    @Override
    public List<DeptDTO> listDepts(DeptQuery query) {
        List<DeptDTO> depts = ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), DeptDTO.class);
        // 如果为空，就代表为自定义权限或者本级权限，就需要去重，不理解可以注释掉，看查询结果
        if (StringUtils.isBlank(SecurityUtils.getDataScopeType())) {
            return deduplication(depts);
        }
        return depts;
    }

    @Override
    public List<DeptDTO> listDeptsByRoleId(Long roleId) {
        Set<Dept> depts = baseMapper.selectDeptListByRoleId(roleId);
        return depts.stream().map(dept -> ConvertUtils.convert(dept, DeptDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<DeptDTO> listDeptsChildren(List<DeptDTO> childrenList, List<DeptDTO> depts) {
        for (DeptDTO dept : depts) {
            depts.add(dept);
            List<DeptDTO> deptList = listDepts(new DeptQuery(dept.getDeptId()));
            if (CollUtil.isNotEmpty(deptList)) {
                listDeptsChildren(deptList, depts);
            }
        }
        return depts;
    }

    @Override
    public List<DeptDTO> listDeptsSuperior(DeptDTO resource, List<DeptDTO> results) {
        Long pid = resource.getPid();
        if (SystemConstant.TOP_ID.equals(pid)) {
            results.addAll(listDepts(new DeptQuery(SystemConstant.TOP_ID)));
            return results;
        }
        results.addAll(listDepts(new DeptQuery(pid)));
        return listDeptsSuperior(getDeptById(pid), results);
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public DeptDTO getDeptById(Long id) {
        Dept dept = baseMapper.selectById(id);
        ValidationUtils.notNull(dept, "Dept", "deptId", id);
        return ConvertUtils.convert(dept, DeptDTO.class);
    }

    @Override
    public List<DeptDTO> buildTree(List<DeptDTO> resources) {
        // 顶级数据
        List<DeptDTO> trees = CollUtil.newArrayList();
        // 子级ID
        Set<Long> ids = CollUtil.newHashSet();
        for (DeptDTO dept : resources) {
            // 添加到顶级列表
            if (SystemConstant.TOP_ID.equals(dept.getPid())) {
                trees.add(dept);
            }
            // 如果是子节点
            for (DeptDTO dept1 : resources) {
                if (dept.getDeptId().equals(dept1.getPid())) {
                    if (ObjectUtil.isNull(dept.getChildren())) {
                        dept.setChildren(CollUtil.newArrayList());
                    }
                    dept.getChildren().add(dept1);
                    ids.add(dept1.getDeptId());
                }
            }
        }
        // 过滤无效数据
        if (CollUtil.isEmpty(trees)) {
            trees = resources.stream().filter(s -> !ids.contains(s.getDeptId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public void verification(Collection<Long> ids) {
        if (userMapper.countUserIdByDeptIds(ids) > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if (roleDeptService.countRoleIdByDeptIds(ids) > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    @Override
    public void exportDept(List<DeptDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(dept -> {
            Map<String, Object> map = MapUtil.newHashMap(3, true);
            map.put("部门名称", dept.getName());
            map.put("部门状态", SystemConstant.YES.equals(dept.getEnabled()) ? "启用" : "停用");
            map.put("创建日期", dept.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 更新子节点
     *
     * @param deptId 部门Id
     */
    private void updateSubCnt(Long deptId) {
        if (deptId != null) {
            int count = lambdaQuery().eq(Dept::getPid, deptId).count();
            lambdaUpdate().eq(Dept::getDeptId, deptId).set(Dept::getSubCount, count).update();
        }
    }

    /**
     * 重复数据删除
     *
     * @param deptList 部门集合
     * @return 操作结果
     */
    private List<DeptDTO> deduplication(List<DeptDTO> deptList) {
        List<DeptDTO> depts = CollUtil.newArrayList();
        for (DeptDTO dept : deptList) {
            boolean flag = true;
            for (DeptDTO dept1 : deptList) {
                if (dept1.getDeptId().equals(dept.getPid())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                depts.add(dept);
            }
        }
        return depts;
    }

    /**
     * 清理缓存
     *
     * @param deptId 部门Id
     */
    private void delCaches(Long deptId, Long oldPid, Long newPid) {
        if (deptId != null) {
            List<User> users = userMapper.selectUserListByDeptId(deptId);
            // 删除数据权限
            redisUtils.delByKeys(CacheKey.DATA_USER, users.stream().map(User::getDeptId).collect(Collectors.toSet()));
            redisUtils.del(CacheKey.DEPT_ID + deptId);
        }
        if (oldPid != null) {
            redisUtils.del(CacheKey.DEPT_PID + oldPid);
        }
        if (newPid != null) {
            redisUtils.del(CacheKey.DEPT_PID + newPid);
        }
    }
}
