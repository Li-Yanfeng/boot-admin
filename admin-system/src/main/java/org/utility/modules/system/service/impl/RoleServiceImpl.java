package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.base.impl.ServiceImpl;
import org.utility.constant.CacheConsts;
import org.utility.exception.BadRequestException;
import org.utility.exception.EntityExistException;
import org.utility.modules.system.mapper.*;
import org.utility.modules.system.model.*;
import org.utility.modules.system.service.*;
import org.utility.modules.system.service.dto.*;
import org.utility.util.ConvertUtils;
import org.utility.util.FileUtils;
import org.utility.util.RedisUtils;

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
 * @since 2021-06-28
 */
@Service
@CacheConfig(cacheNames = {"role"})
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDTO, RoleQuery, Role> implements RoleService {

    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final UserMapper userMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final RoleDeptMapper roleDeptMapper;
    private final DeptService deptService;
    private final RoleMenuService roleMenuService;
    private final RoleDeptService roleDeptService;
    private final UserRoleService userRoleService;

    private final RedisUtils redisUtils;

    public RoleServiceImpl(RoleMapper roleMapper, MenuMapper menuMapper, UserMapper userMapper,
                           RoleMenuMapper roleMenuMapper, RoleDeptMapper roleDeptMapper, DeptService deptService,
                           RoleMenuService roleMenuService,
                           RoleDeptService roleDeptService, UserRoleService userRoleService, RedisUtils redisUtils) {
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.userMapper = userMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.roleDeptMapper = roleDeptMapper;
        this.deptService = deptService;
        this.roleMenuService = roleMenuService;
        this.roleDeptService = roleDeptService;
        this.userRoleService = userRoleService;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(RoleDTO resource) {
        if (this.getByName(resource.getName()) != null) {
            throw new EntityExistException(Role.class, "name", resource.getName());
        }
        Role newRole = ConvertUtils.convert(resource, Role.class);
        roleMapper.insert(newRole);

        if (resource.getDepts() != null) {
            resource.getDepts().forEach(dept -> {
                RoleDept rd = new RoleDept();
                rd.setRoleId(newRole.getRoleId());
                rd.setDeptId(dept.getDeptId());
                roleDeptMapper.insert(rd);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Collection<Long> ids) {
        roleMapper.deleteBatchIds(ids);
        roleMenuService.removeByRoleIds(ids);
        roleDeptService.removeByRoleIds(ids);
        for (Long id : ids) {
            // 清理缓存
            this.delCaches(id);
        }
    }

    @Override
    public void updateById(RoleDTO resource) {
        Role roleOld = this.getRoleById(resource.getRoleId());
        Role role1 = this.getByName(resource.getName());
        if (role1 != null && ObjectUtil.notEqual(role1.getRoleId(), resource.getRoleId())) {
            throw new EntityExistException(Role.class, "name", resource.getName());
        }
        roleOld.setName(resource.getName());
        roleOld.setDescription(resource.getDescription());
        roleOld.setDataScope(resource.getDataScope());
        roleOld.setLevel(resource.getLevel());
        roleMapper.updateById(roleOld);

        roleDeptService.removeByRoleIds(CollUtil.newHashSet(resource.getRoleId()));
        if (resource.getDepts() != null) {
            resource.getDepts().forEach(dept -> {
                RoleDept rd = new RoleDept();
                rd.setRoleId(resource.getRoleId());
                rd.setDeptId(dept.getDeptId());
                roleDeptMapper.insert(rd);
            });
        }
        // 清理缓存
        redisUtils.del(CacheConsts.ROLE_ID + resource.getRoleId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenu(RoleDTO resource) {
        // 清理缓存
        List<User> users = userMapper.selectByRoleId(resource.getRoleId());
        Set<Long> userIds = users.stream().map(User::getUserId).collect(Collectors.toSet());
        redisUtils.delByKeys(CacheConsts.MENU_USER, userIds);
        redisUtils.del(CacheConsts.ROLE_ID + resource.getRoleId());

        List<Long> oldMenuIds = roleMenuService.listMenuIdByRoleId(resource.getRoleId());
        List<Long> menuIds = resource.getMenus().stream().map(MenuDTO::getMenuId).collect(Collectors.toList());

        List<Long> deleteList = oldMenuIds.stream().filter(item -> !menuIds.contains(item)).collect(Collectors.toList());
        List<Long> addList = menuIds.stream().filter(item -> !oldMenuIds.contains(item)).collect(Collectors.toList());

        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenu::getRoleId, resource.getRoleId());
        if (CollectionUtils.isNotEmpty(deleteList)) {
            wrapper.in(RoleMenu::getMenuId, deleteList);
            roleMenuMapper.delete(wrapper);
        }

        RoleMenu rm = new RoleMenu();
        addList.forEach(item -> {
            rm.setMenuId(item);
            rm.setRoleId(resource.getRoleId());
            roleMenuMapper.insert(rm);
        });
    }

    @Override
    public List<RoleSmallDTO> listByUsersId(Long userId) {
        return ConvertUtils.convertList(roleMapper.selectByUserId(userId), RoleSmallDTO.class);
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public RoleDTO getById(Long id) {
        RoleDTO role = ConvertUtils.convert(this.getRoleById(id), RoleDTO.class);
        role.setMenus(ConvertUtils.convertSet(menuMapper.selectByRoleId(role.getRoleId()), MenuDTO.class));
        role.setDepts(deptService.listByRoleId(role.getRoleId()));
        return role;
    }

    @Override
    public Integer getLevelByRoles(Set<Long> roleIds) {
        if (roleIds.size() == 0) {
            return Integer.MAX_VALUE;
        }
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        return roles.stream().map(Role::getLevel).min(Integer::min).get();
    }

    @Cacheable(key = "'auth:' + #p0.userId")
    @Override
    public List<GrantedAuthority> mapToGrantedAuthorities(UserDTO user) {
        Set<String> permissions = CollUtil.newHashSet();
        // 如果是管理员直接返回
        if (user.getAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        // 获取用户所拥有的角色
        Set<Role> roles = roleMapper.selectByUserId(user.getUserId());
        Set<Long> roleIds = roles.stream().map(Role::getRoleId).collect(Collectors.toSet());
        // 获取角色所拥有的菜单
        List<Menu> menus = menuMapper.selectByRoleIds(roleIds);
        permissions = menus.stream().map(Menu::getPermission).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userRoleService.countByRoleIds(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public void download(HttpServletResponse response, List<RoleDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (RoleDTO role : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(4, true);
            map.put("名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 根据 roleId 查询
     *
     * @param roleId 角色Id
     * @return /
     */
    private Role getRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    /**
     * 根据 name 查询
     *
     * @param name 角色名称
     * @return /
     */
    private Role getByName(String name) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getName, name);
        return roleMapper.selectOne(wrapper);
    }


    /**
     * 清理缓存
     *
     * @param roleId 角色ID
     */
    private void delCaches(Long roleId) {
        List<User> users = userMapper.selectByRoleId(roleId);
        Set<Long> userIds = users.stream().map(User::getUserId).collect(Collectors.toSet());
        redisUtils.delByKeys(CacheConsts.DATA_USER, userIds);
        redisUtils.delByKeys(CacheConsts.MENU_USER, userIds);
        redisUtils.delByKeys(CacheConsts.ROLE_AUTH, userIds);
        redisUtils.del(CacheConsts.ROLE_ID + roleId);
    }
}
