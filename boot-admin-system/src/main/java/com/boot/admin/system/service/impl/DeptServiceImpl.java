package com.boot.admin.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.system.mapper.DeptMapper;
import com.boot.admin.system.mapper.UserMapper;
import com.boot.admin.system.model.Dept;
import com.boot.admin.system.model.User;
import com.boot.admin.system.service.DeptService;
import com.boot.admin.system.service.RoleDeptService;
import com.boot.admin.system.service.dto.DeptDTO;
import com.boot.admin.system.service.dto.DeptQuery;
import com.boot.admin.util.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Long pid = resource.getPid();
        Dept superiorDept = baseMapper.selectById(pid);
        // 如果父节点处于停用状态,则不允许新增子节点
        if (ObjectUtil.isNotNull(superiorDept) && CommonConstant.UN_ENABLE.equals(superiorDept.getEnabled())) {
            throw new BadRequestException(UserErrorCode.SUPERIOR_NODE_DEACTIVATED_NOT_ALLOWED_TO_ADD);
        }
        // 填充属性
        String ancestors = ObjectUtil.isNull(superiorDept)
            ? pid.toString()
            : superiorDept.getAncestors() + StrUtil.COMMA + pid;
        resource.setAncestors(ancestors);

        baseMapper.insert(resource);
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
        if (deptId.equals(newPid)) {
            throw new BadRequestException("上级不能为自己");
        }

        Dept superiorDept = baseMapper.selectById(newPid);
        // 填充属性
        String ancestors = ObjectUtil.isNull(superiorDept)
            ? newPid.toString()
            : superiorDept.getAncestors() + StrUtil.COMMA + newPid;
        resource.setAncestors(ancestors);
        baseMapper.updateById(resource);

        // 如果该部门是启用状态，则启用该部门的所有上级部门
        if (CommonConstant.ENABLE.equals(resource.getEnabled()) && !CommonConstant.TOP_ID.toString().equals(ancestors)) {
            List<Long> superiorIds = listDeptsSuperiorIds(ancestors);
            lambdaUpdate().set(Dept::getEnabled, CommonConstant.ENABLE).in(Dept::getDeptId, superiorIds).update();
        }

        // 清理缓存
        delCaches(deptId, oldPid, newPid);
    }

    @Cacheable(key = "'pid:' + #p0.pid", condition = "#p0.pid != null")
    @Override
    public List<DeptDTO> listDepts(DeptQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), DeptDTO.class);
    }

    @Override
    public List<DeptDTO> listDeptsByRoleId(Long roleId) {
        return ConvertUtils.convert(baseMapper.selectDeptListByRoleId(roleId), DeptDTO.class);
    }

    @Override
    public List<DeptDTO> listDeptsChildren(Long id, boolean containsItself) {
        String applySql = SqlFunctionUtils.findInSet(id, "ancestors");
        List<Dept> depts = lambdaQuery().eq(containsItself, Dept::getDeptId, id).or().apply(applySql).list();
        return ConvertUtils.convert(depts, DeptDTO.class);
    }

    @Override
    public List<DeptDTO> listDeptsSuperior(Long id) {
        Dept dept = baseMapper.selectById(id);
        ValidationUtils.notNull(dept, "Dept", "deptId", id);
        List<Long> superiorDeptIds = listDeptsSuperiorIds(dept.getAncestors());
        return ConvertUtils.convert(baseMapper.selectBatchIds(superiorDeptIds), DeptDTO.class);
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public DeptDTO getDeptById(Long id) {
        Dept dept = baseMapper.selectById(id);
        ValidationUtils.notNull(dept, "Dept", "deptId", id);
        return ConvertUtils.convert(dept, DeptDTO.class);
    }

    @Override
    public List<Tree<Long>> buildTree(Collection<DeptDTO> resources) {
        // 移除重复数据
        List<DeptDTO> list = resources.stream().distinct().collect(Collectors.toList());
        // 最顶层ID
        Long pid = list.stream().map(DeptDTO::getPid).min(Long::compareTo).orElseGet(() -> CommonConstant.TOP_ID);
        // 属性名配置字段
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig()
            .setIdKey("deptId")
            .setParentIdKey("pid")
            .setWeightKey("deptSort")
            .setNameKey("name")
            .setChildrenKey("children");
        // 转换器
        return TreeUtil.build(list, pid, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getDeptId());
            tree.setParentId(treeNode.getPid());
            tree.setWeight(treeNode.getDeptSort());
            tree.setName(treeNode.getName());
        });
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
            map.put("部门状态", CommonConstant.YES.equals(dept.getEnabled()) ? "启用" : "停用");
            map.put("创建日期", dept.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 根据 ancestors 获取上级节点列表
     *
     * @param ancestors 组级列表
     * @return 上级节点列表（不包含自己）
     */
    private List<Long> listDeptsSuperiorIds(String ancestors) {
        return Stream.of(ancestors.split(StrUtil.COMMA)).map(Long::parseLong).collect(Collectors.toList());
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
        // 清理上级缓存
        if (oldPid != null) {
            delSuperiorCaches(oldPid);
        }
        if (newPid != null) {
            delSuperiorCaches(newPid);
        }
    }

    /**
     * 清理上级缓存
     *
     * @param pid 上级部门Id
     */
    private void delSuperiorCaches(Long pid) {
        redisUtils.del(CacheKey.DEPT_PID + pid);
        Dept dept = lambdaQuery().eq(Dept::getDeptId, pid).one();
        if (ObjectUtil.isNotNull(dept)) {
            delSuperiorCaches(dept.getPid());
        }
    }
}
