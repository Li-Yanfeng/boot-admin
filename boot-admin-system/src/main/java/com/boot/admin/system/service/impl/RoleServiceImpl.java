package com.boot.admin.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.EntityExistException;
import com.boot.admin.security.service.UserCacheClean;
import com.boot.admin.system.mapper.*;
import com.boot.admin.system.model.*;
import com.boot.admin.system.service.RoleDeptService;
import com.boot.admin.system.service.RoleMenuService;
import com.boot.admin.system.service.RoleService;
import com.boot.admin.system.service.UserRoleService;
import com.boot.admin.system.service.dto.*;
import com.boot.admin.util.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"role"})
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final MenuMapper menuMapper;
    private final UserMapper userMapper;
    private final DeptMapper deptMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final RoleDeptMapper roleDeptMapper;
    private final RoleMenuService roleMenuService;
    private final RoleDeptService roleDeptService;
    private final UserRoleService userRoleService;
    private final UserCacheClean userCacheClean;

    private final RedisUtils redisUtils;

    public RoleServiceImpl(MenuMapper menuMapper, UserMapper userMapper, DeptMapper deptMapper,
                           RoleMenuMapper roleMenuMapper, RoleDeptMapper roleDeptMapper,
                           RoleMenuService roleMenuService, RoleDeptService roleDeptService,
                           UserRoleService userRoleService, UserCacheClean userCacheClean, RedisUtils redisUtils) {
        this.menuMapper = menuMapper;
        this.userMapper = userMapper;
        this.deptMapper = deptMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.roleDeptMapper = roleDeptMapper;
        this.roleMenuService = roleMenuService;
        this.roleDeptService = roleDeptService;
        this.userRoleService = userRoleService;
        this.userCacheClean = userCacheClean;
        this.redisUtils = redisUtils;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRole(RoleDTO resource) {
        if (ObjectUtil.isNotNull(lambdaQuery().eq(Role::getName, resource.getName()).one())) {
            throw new EntityExistException(resource.getName());
        }
        Role role = ConvertUtils.convert(resource, Role.class);
        baseMapper.insert(role);

        if (CollUtil.isNotEmpty(resource.getDepts())) {
            resource.getDepts().forEach(dept -> {
                RoleDept rd = new RoleDept();
                rd.setRoleId(resource.getRoleId());
                rd.setDeptId(dept.getDeptId());
                roleDeptMapper.insert(rd);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeRoleByIds(Collection<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            delCaches(id);
        }
        roleMenuService.removeRoleMenuByRoleIds(ids);
        roleDeptService.removeRoleDeptByRoleIds(ids);
        baseMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleById(RoleDTO resource) {
        Long roleId = resource.getRoleId();
        Assert.notNull(baseMapper.selectById(roleId));
        Role role1 = lambdaQuery().eq(Role::getName, resource.getName()).one();
        if (ObjectUtil.isNotNull(role1) && ObjectUtil.notEqual(roleId, role1.getRoleId())) {
            throw new EntityExistException(resource.getName());
        }

        List<DeptDTO> depts = resource.getDepts();
        if (CollUtil.isNotEmpty(depts)) {
            // 更新数据
            List<Long> oldDeptIds = roleDeptService.listDeptIdsByRoleId(roleId);
            List<Long> newDeptIds = depts.stream().map(DeptDTO::getDeptId).collect(Collectors.toList());
            List<Long> delDeptIds = ComparatorUtils.findDataNotIncludedInSourceData(oldDeptIds, newDeptIds);
            List<Long> addDeptIds = ComparatorUtils.findDataNotIncludedInSourceData(newDeptIds, oldDeptIds);
            if (CollUtil.isNotEmpty(delDeptIds)) {
                roleDeptService.removeRoleDeptByRoleIdEqAndDeptIdsIn(roleId, delDeptIds);
            }
            if (CollUtil.isNotEmpty(addDeptIds)) {
                addDeptIds.forEach(deptId -> {
                    RoleDept rd = new RoleDept();
                    rd.setRoleId(resource.getRoleId());
                    rd.setDeptId(deptId);
                    roleDeptMapper.insert(rd);
                });
            }
        }

        Role role = ConvertUtils.convert(resource, Role.class);
        baseMapper.updateById(role);
        // 清理缓存
        delCaches(resource.getRoleId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenu(RoleDTO newResource, RoleDTO oldResource) {
        Long roleId = newResource.getRoleId();
        Assert.notNull(baseMapper.selectById(roleId));
        // 清理缓存
        delCaches(newResource.getRoleId());
        // 更新数据
        List<Long> oldMenuIds = roleMenuService.listMenuIdsByRoleId(roleId);
        List<Long> newMenuIds = newResource.getMenus().stream().map(MenuDTO::getMenuId).collect(Collectors.toList());
        List<Long> delMenuIds = ComparatorUtils.findDataNotIncludedInSourceData(oldMenuIds, newMenuIds);
        List<Long> addMenuIds = ComparatorUtils.findDataNotIncludedInSourceData(newMenuIds, oldMenuIds);
        if (CollUtil.isNotEmpty(delMenuIds)) {
            roleMenuService.removeRoleMenuByRoleIdEqAndMenuIdsIn(roleId, delMenuIds);
        }
        if (CollUtil.isNotEmpty(addMenuIds)) {
            addMenuIds.forEach(menuId -> {
                RoleMenu rm = new RoleMenu();
                rm.setMenuId(menuId);
                rm.setRoleId(roleId);
                roleMenuMapper.insert(rm);
            });
        }
    }

    @Override
    public List<RoleDTO> listRoles(RoleQuery query) {
        List<RoleDTO> roles = ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), RoleDTO.class);
        // 获取关联数据
        roles.forEach(this::getRelevantData);
        return roles;
    }

    @Override
    public Page<RoleDTO> listRoles(RoleQuery query, Page<Role> page) {
        Page<RoleDTO> roles = ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)),
            RoleDTO.class);
        List<RoleDTO> records = roles.getRecords();
        // 获取关联数据
        records.forEach(this::getRelevantData);
        return roles;
    }

    @Override
    public List<RoleSmallDTO> listRolesByUserId(Long userId) {
        return ConvertUtils.convert(baseMapper.selectRoleListByUserId(userId), RoleSmallDTO.class);
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public RoleDTO getRoleById(Long id) {
        Role role = baseMapper.selectById(id);
        Assert.notNull(role);
        RoleDTO roleDTO = ConvertUtils.convert(role, RoleDTO.class);
        // 获取关联数据
        getRelevantData(roleDTO);
        return roleDTO;
    }

    @Override
    public Integer getLevelByRoles(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Integer.MAX_VALUE;
        }
        List<Role> roles = baseMapper.selectBatchIds(roleIds);
        return roles.stream().map(Role::getLevel).min(Integer::min).get();
    }

    @Cacheable(key = "'auth:' + #p0.userId")
    @Override
    public List<GrantedAuthority> mapToGrantedAuthorities(UserDTO user) {
        Set<String> permissions = CollUtil.newHashSet();
        // 如果是管理员直接返回
        if (CommonConstant.YES.equals(user.getAdmin())) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        // 获取用户所拥有的角色
        List<Role> roles = baseMapper.selectRoleListByUserId(user.getUserId());
        Set<Long> roleIds = roles.stream().map(Role::getRoleId).collect(Collectors.toSet());
        // 获取角色所拥有的菜单
        List<Menu> menus = menuMapper.selectMenuListByRoleIds(roleIds);
        permissions = menus.stream().map(Menu::getPermission).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public void verification(Collection<Long> ids) {
        if (userRoleService.countUserIdByRoleIds(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public void exportRole(List<RoleDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(role -> {
            Map<String, Object> map = MapUtil.newHashMap(4, true);
            map.put("名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 获取关联数据
     */
    private void getRelevantData(RoleDTO roleDTO) {
        Long roleId = roleDTO.getRoleId();
        roleDTO.setMenus(ConvertUtils.convert(menuMapper.selectMenuListByRoleId(roleId), MenuDTO.class));
        roleDTO.setDepts(ConvertUtils.convert(deptMapper.selectDeptListByRoleId(roleId), DeptDTO.class));
    }

    /**
     * 清理缓存
     *
     * @param roleId 角色ID
     */
    private void delCaches(Long roleId) {
        List<User> users = userMapper.selectUserListByRoleId(roleId);
        Set<Long> userIds = users.stream().map(User::getUserId).collect(Collectors.toSet());
        users.forEach(item -> userCacheClean.cleanUserCache(item.getUsername()));
        redisUtils.delByKeys(CacheKey.DATA_USER, userIds);
        redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
        redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds);
        redisUtils.del(CacheKey.ROLE_ID + roleId);
    }
}
