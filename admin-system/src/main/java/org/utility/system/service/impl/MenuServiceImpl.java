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
import org.utility.exception.EntityExistException;
import org.utility.system.mapper.MenuMapper;
import org.utility.system.mapper.RoleMapper;
import org.utility.system.mapper.UserMapper;
import org.utility.system.model.Menu;
import org.utility.system.model.Role;
import org.utility.system.model.User;
import org.utility.system.model.vo.MenuMetaVO;
import org.utility.system.model.vo.MenuVO;
import org.utility.system.service.MenuService;
import org.utility.system.service.RoleMenuService;
import org.utility.system.service.RoleService;
import org.utility.system.service.dto.MenuDTO;
import org.utility.system.service.dto.MenuQuery;
import org.utility.system.service.dto.RoleSmallDTO;
import org.utility.util.*;
import org.utility.util.enums.MenuType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"menu"})
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final RoleService roleService;
    private final RoleMenuService roleMenuService;

    private final RedisUtils redisUtils;

    public MenuServiceImpl(UserMapper userMapper, RoleMapper roleMapper, RoleService roleService,
                           RoleMenuService roleMenuService, RedisUtils redisUtils) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.roleService = roleService;
        this.roleMenuService = roleMenuService;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMenu(Menu resource) {
        if (ObjectUtil.isNotNull(getMenuByTitle(resource.getTitle()))) {
            throw new EntityExistException(Menu.class, "title", resource.getTitle());
        }
        if (StringUtils.isNotBlank(resource.getComponentName())) {
            if (ObjectUtil.isNotNull(getMenuByComponentName(resource.getComponentName()))) {
                throw new EntityExistException(Menu.class, "componentName", resource.getComponentName());
            }
        }
        if (SystemConstant.YES.equals(resource.getIFrame())) {
            if (!(resource.getPath().toLowerCase().startsWith(SystemConstant.HTTP) || resource.getPath().toLowerCase().startsWith(SystemConstant.HTTPS))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        // 计算子节点数目
        resource.setSubCount(SystemConstant.NO_RECORD);
        baseMapper.insert(resource);
        // 更新父节点菜单数目
        updateSubCnt(resource.getPid());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeMenu(Collection<MenuDTO> resources) {
        for (MenuDTO menu : resources) {
            Long menuId = menu.getMenuId();
            // 清理缓存
            delCaches(menuId);
            // 解绑菜单
            roleMenuService.removeRoleMenuByMenuIds(CollUtil.newHashSet(menuId));
            // 删除当前节点
            baseMapper.deleteById(menuId);
            // 更新父节点菜单数目
            updateSubCnt(menu.getPid());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenuById(Menu resource) {
        Long menuId = resource.getMenuId();
        Menu menu = baseMapper.selectById(menuId);
        ValidationUtils.notNull(menu, "Menu", "menuId", menuId);

        if (menuId.equals(resource.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        if (SystemConstant.YES.equals(resource.getIFrame())) {
            if (!(resource.getPath().toLowerCase().startsWith(SystemConstant.HTTP) || resource.getPath().toLowerCase().startsWith(SystemConstant.HTTPS))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }

        Menu menu1 = getMenuByTitle(resource.getTitle());
        if (ObjectUtil.isNotNull(menu1) && ObjectUtil.notEqual(menu.getMenuId(), menu1.getMenuId())) {
            throw new EntityExistException(Menu.class, "title", resource.getTitle());
        }
        if (StringUtils.isNotBlank(resource.getComponentName())) {
            menu1 = getMenuByComponentName(resource.getComponentName());
            if (ObjectUtil.isNotNull(menu1) && ObjectUtil.notEqual(menu.getMenuId(), menu1.getMenuId())) {
                throw new EntityExistException(Menu.class, "componentName", resource.getComponentName());
            }
        }

        // 记录父节点ID
        Long oldPid = menu.getPid();
        Long newPid = resource.getPid();

        // 类型从菜单或按钮变更为目录，清空路径和权限
        if (ObjectUtil.notEqual(menu.getType(), MenuType.FOLDER.getValue()) && resource.getType().equals(MenuType.FOLDER.getValue())) {
            menu.setComponent(null);
            menu.setComponentName(null);
            menu.setPermission(null);
        } else {
            menu.setComponent(resource.getComponent());
            menu.setComponentName(resource.getComponentName());
            menu.setPermission(resource.getPermission());
        }
        baseMapper.updateById(resource);
        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
        delCaches(menuId);
    }

    @Cacheable(key = "'pid:' + #p0.pid", condition = "#p0.pid != null")
    @Override
    public List<MenuDTO> listMenus(MenuQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), MenuDTO.class);
    }

    @Override
    public List<MenuDTO> listMenusByUserId(Long userId) {
        List<RoleSmallDTO> roles = roleService.listRolesByUserId(userId);
        Set<Long> roleIds = roles.stream().map(RoleSmallDTO::getRoleId).collect(Collectors.toSet());
        LinkedHashSet<Menu> menus = baseMapper.selectMenuListByRoleIdsAndType(roleIds, 2L);
        return menus.stream().map(menu -> ConvertUtils.convert(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<MenuDTO> listMenusChildren(List<MenuDTO> childrenList, List<MenuDTO> menus) {
        for (MenuDTO menu : menus) {
            menus.add(menu);
            List<MenuDTO> menuList = listMenus(new MenuQuery(menu.getMenuId()));
            if (CollUtil.isNotEmpty(menuList)) {
                listMenusChildren(menuList, menus);
            }
        }
        return menus;
    }

    @Override
    public List<MenuDTO> listMenusSuperior(MenuDTO resource, List<MenuDTO> results) {
        Long pid = resource.getPid();
        if (SystemConstant.TOP_ID.equals(pid)) {
            results.addAll(listMenus(new MenuQuery(SystemConstant.TOP_ID)));
            return results;
        }
        results.addAll(listMenus(new MenuQuery(pid)));
        return listMenusSuperior(getMenuById(pid), results);
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public MenuDTO getMenuById(Long id) {
        Menu menu = baseMapper.selectById(id);
        ValidationUtils.notNull(menu, "Menu", "menuId", id);
        return ConvertUtils.convert(menu, MenuDTO.class);
    }

    @Override
    public List<MenuDTO> buildTree(List<MenuDTO> resources) {
        // 顶级数据
        List<MenuDTO> trees = CollUtil.newArrayList();
        // 子级ID
        Set<Long> ids = CollUtil.newHashSet();
        for (MenuDTO menu : resources) {
            // 添加到顶级列表
            if (SystemConstant.TOP_ID.equals(menu.getPid())) {
                trees.add(menu);
            }
            // 如果是子节点
            for (MenuDTO menu1 : resources) {
                if (menu.getMenuId().equals(menu1.getPid())) {
                    if (ObjectUtil.isNull(menu.getChildren())) {
                        menu.setChildren(CollUtil.newArrayList());
                    }
                    menu.getChildren().add(menu1);
                    ids.add(menu1.getMenuId());
                }
            }
        }
        // 过滤无效数据
        if (CollUtil.isEmpty(trees)) {
            trees = resources.stream().filter(s -> !ids.contains(s.getMenuId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<MenuVO> buildMenus(List<MenuDTO> resources) {
        List<MenuVO> list = CollUtil.newLinkedList();
        resources.forEach(menu -> {
            if (ObjectUtil.isNotNull(menu)) {
                List<MenuDTO> childrens = menu.getChildren();
                MenuVO menuVO = new MenuVO();
                menuVO.setName(StringUtils.isNotBlank(menu.getComponentName()) ? menu.getComponentName() :
                    menu.getTitle());
                // 一级目录需要加斜杠，不然会报警告
                menuVO.setPath(SystemConstant.TOP_ID.equals(menu.getPid()) ? "/" + menu.getPath() : menu.getPath());
                menuVO.setHidden(menu.getHidden());
                // 如果不是外链
                if (SystemConstant.NO.equals(menu.getIFrame())) {
                    if (SystemConstant.TOP_ID.equals(menu.getPid())) {
                        menuVO.setComponent(StringUtils.isNotBlank(menu.getComponent()) ? menu.getComponent() :
                            "Layout");
                    } else if (MenuType.FOLDER.getValue().equals(menu.getType())) {
                        // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                        menuVO.setComponent(StringUtils.isNotBlank(menu.getComponent()) ? menu.getComponent() :
                            "ParentView");
                    } else if (!StringUtils.isNotBlank(menu.getComponent())) {
                        menuVO.setComponent(menu.getComponent());
                    }
                }
                menuVO.setMeta(new MenuMetaVO(menu.getTitle(), menu.getIcon(), menu.getCache()));
                if (CollUtil.isNotEmpty(childrens)) {
                    menuVO.setAlwaysShow(true);
                    menuVO.setRedirect("noredirect");
                    menuVO.setChildren(buildMenus(childrens));
                } else if (SystemConstant.TOP_ID.equals(menu.getPid())) {
                    // 处理是一级菜单并且没有子菜单的情况
                    MenuVO menuVO1 = new MenuVO();
                    menuVO1.setMeta(menuVO.getMeta());
                    // 如果不是外链
                    if (SystemConstant.NO.equals(menu.getIFrame())) {
                        menuVO1.setPath("index");
                        menuVO1.setName(menuVO.getName());
                        menuVO1.setComponent(menuVO.getComponent());
                    } else {
                        menuVO1.setPath(menu.getPath());
                    }
                    menuVO.setName(null);
                    menuVO.setMeta(null);
                    menuVO.setComponent("Layout");
                    menuVO.setChildren(CollUtil.newArrayList(menuVO1));
                }
                list.add(menuVO);
            }
        });
        return list;
    }

    @Override
    public void exportMenu(List<MenuDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(menu -> {
            Map<String, Object> map = MapUtil.newHashMap(13, true);
            map.put("上级菜单ID", menu.getPid());
            map.put("菜单类型", menu.getType());
            map.put("菜单标题", menu.getTitle());
            map.put("组件", menu.getComponent());
            map.put("组件名称", menu.getComponentName());
            map.put("图标", menu.getIcon());
            map.put("权限", menu.getPermission());
            map.put("是否外链", menu.getIFrame());
            map.put("链接地址", menu.getPath());
            map.put("是否缓存", menu.getCache());
            map.put("是否隐藏", menu.getHidden());
            map.put("排序", menu.getSort());
            map.put("子菜单数目", menu.getSubCount());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 更新 menu 下节点数量
     *
     * @param menuId 菜单ID
     */
    private void updateSubCnt(Long menuId) {
        if (menuId != null) {
            int count = lambdaQuery().eq(Menu::getPid, menuId).count();
            lambdaUpdate().eq(Menu::getMenuId, menuId).set(Menu::getSubCount, count).update();
        }
    }

    /**
     * 清理缓存
     *
     * @param menuId 菜单ID
     */
    private void delCaches(Long menuId) {
        redisUtils.del(CacheKey.MENU_ID + menuId);
        List<User> users = userMapper.selectUserListByMenuId(menuId);
        redisUtils.delByKeys(CacheKey.MENU_USER, users.stream().map(User::getUserId).collect(Collectors.toSet()));
        List<Role> roles = roleMapper.selectRoleListByMenuId(menuId);
        redisUtils.delByKeys(CacheKey.ROLE_ID, roles.stream().map(Role::getRoleId).collect(Collectors.toSet()));
    }


    /**
     * 根据 title 查询
     *
     * @param title 菜单标题
     */
    private Menu getMenuByTitle(String title) {
        return lambdaQuery().eq(Menu::getTitle, title).one();
    }

    /**
     * 根据 componentName 查询
     *
     * @param componentName 组件名称
     */
    private Menu getMenuByComponentName(String componentName) {
        return lambdaQuery().eq(Menu::getComponentName, componentName).one();
    }
}
