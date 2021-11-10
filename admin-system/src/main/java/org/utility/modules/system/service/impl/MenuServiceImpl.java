package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.constant.CacheConsts;
import org.utility.exception.BadRequestException;
import org.utility.exception.EntityExistException;
import org.utility.modules.system.mapper.MenuMapper;
import org.utility.modules.system.mapper.UserMapper;
import org.utility.modules.system.model.Menu;
import org.utility.modules.system.model.User;
import org.utility.modules.system.model.vo.MenuMetaVO;
import org.utility.modules.system.model.vo.MenuVO;
import org.utility.modules.system.service.MenuService;
import org.utility.modules.system.service.RoleMenuService;
import org.utility.modules.system.service.RoleService;
import org.utility.modules.system.service.dto.MenuDTO;
import org.utility.modules.system.service.dto.MenuQuery;
import org.utility.modules.system.service.dto.RoleSmallDTO;
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
 * @since 2021-06-28
 */
@Service
@CacheConfig(cacheNames = {"menu"})
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDTO, MenuQuery, Menu> implements MenuService {

    private final MenuMapper menuMapper;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final RoleMenuService roleMenuService;

    private final RedisUtils redisUtils;

    public MenuServiceImpl(MenuMapper menuMapper, UserMapper userMapper, RoleService roleService,
                           RoleMenuService roleMenuService, RedisUtils redisUtils) {
        this.menuMapper = menuMapper;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.roleMenuService = roleMenuService;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Menu resource) {
        if (getByTitle(resource.getTitle()) != null) {
            throw new EntityExistException(Menu.class, "title", resource.getTitle());
        }

        if (StrUtil.isNotBlank(resource.getComponentName())) {
            if (getByComponentName(resource.getComponentName()) != null) {
                throw new EntityExistException(Menu.class, "componentName", resource.getComponentName());
            }
        }

        if (resource.getiFrame()) {
            String http = "http://", https = "https://";
            if (!(resource.getPath().toLowerCase().startsWith(http) || resource.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }

        if (ObjectUtil.equal(resource.getPid(), 0L)) {
            resource.setPid(null);
        }

        // 计算子节点数目
        resource.setSubCount(resource.getSubCount() == null ? 0 : resource.getSubCount());
        resource.setMenuSort(resource.getMenuSort() == null ? 999 : resource.getMenuSort());
        menuMapper.insert(resource);

        // 计算子节点数目
        if (resource.getPid() != null) {
            // 清理缓存
            updateSubCnt(resource.getPid());
        }
        // 清理缓存
        if (resource.getPid() != null) {
            redisUtils.del(CacheConsts.MENU_PID + (resource.getPid() == null ? 0 : resource.getPid()));
        }
        List<String> keys = redisUtils.scan(CacheConsts.MENU_USER + "*");
        keys.forEach(redisUtils::del);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Collection<Long> ids) {
        roleMenuService.removeByMenuIds(ids);
        for (Long id : ids) {
            MenuDTO menu = getById(id);
            delCaches(menu.getMenuId(), menu.getPid());
            if (menu.getPid() != null) {
                updateSubCnt(menu.getPid());
            }
        }
        menuMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateById(Menu resource) {
        if (resource.getMenuId().equals(resource.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Menu menu = menuMapper.selectById(resource.getMenuId());
        ValidationUtils.notNull(menu.getMenuId(), "Permission", "id", resource.getMenuId());
        // 记录旧的父节点ID
        Long pid = menu.getPid();

        if (resource.getiFrame()) {
            String http = "http://", https = "https://";
            if (!(resource.getPath().toLowerCase().startsWith(http) || resource.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }

        Menu menu1 = getByTitle(resource.getTitle());
        if (menu1 != null && ObjectUtil.notEqual(menu1.getMenuId(), menu.getMenuId())) {
            throw new EntityExistException(Menu.class, "name", resource.getTitle());
        }

        if (ObjectUtil.equal(resource.getPid(), 0L)) {
            resource.setPid(null);
        }

        if (StringUtils.isNotBlank(resource.getComponentName())) {
            menu1 = getByComponentName(resource.getComponentName());
            if (menu1 != null && ObjectUtil.notEqual(menu1.getMenuId(), menu.getMenuId())) {
                throw new EntityExistException(Menu.class, "componentName", resource.getComponentName());
            }
        }

        // 记录的父节点ID
        Long oldPid = menu.getPid();
        Long newPid = resource.getPid();

        // 类型从菜单或按钮变更为目录，清空路径和权限
        if (menu.getType().equals(MenuType.FOLDER.getValue()) && resource.getType().equals(MenuType.FOLDER.getValue())) {
            menu.setComponent(null);
            menu.setPermission(null);
            menu.setComponentName(null);
        } else {
            menu.setComponent(resource.getComponent());
            menu.setPermission(resource.getPermission());
            menu.setComponentName(resource.getComponentName());
        }
        menu.setTitle(resource.getTitle());
        menu.setPath(resource.getPath());
        menu.setIcon(resource.getIcon());
        menu.setiFrame(resource.getiFrame());
        menu.setPid(resource.getPid());
        menu.setMenuSort(resource.getMenuSort());
        menu.setCache(resource.getCache());
        menu.setHidden(resource.getHidden());
        menu.setType(resource.getType());
        menuMapper.updateById(menu);

        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
        delCaches(resource.getMenuId(), pid);
    }

    @Override
    public List<MenuDTO> list(MenuQuery query) {
        QueryWrapper<Menu> wrapper = QueryHelp.queryWrapper(query);
        return ConvertUtils.convertList(menuMapper.selectList(wrapper), MenuDTO.class);
    }

    @Cacheable(key = "'pid:' + #p0")
    @Override
    public List<MenuDTO> listByPid(Long pid) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        if (pid != null && !pid.equals(0L)) {
            wrapper.eq(Menu::getPid, pid).orderByAsc(Menu::getMenuSort);
        } else {
            wrapper.isNull(Menu::getPid).orderByAsc(Menu::getMenuSort);
        }
        return ConvertUtils.convertList(menuMapper.selectList(wrapper), MenuDTO.class);
    }

    @Override
    public List<MenuDTO> listByUserId(Long userId) {
        List<RoleSmallDTO> roles = roleService.listByUsersId(userId);
        Set<Long> roleIds = roles.stream().map(RoleSmallDTO::getRoleId).collect(Collectors.toSet());
        LinkedHashSet<Menu> menus = menuMapper.selectByRoleIdsAndType(roleIds, 2L);
        return ConvertUtils.convertList(menus, MenuDTO.class);
    }

    @Override
    public Set<Menu> listChildMenu(List<Menu> menuList, Set<Menu> menuSet) {
        // 递归找出子的菜单(包含自己)
        for (Menu menu : menuList) {
            menuSet.add(menu);
            LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Menu::getPid, menu.getMenuId());
            List<Menu> menus = menuMapper.selectList(wrapper);
            if (CollUtil.isNotEmpty(menus)) {
                listChildMenu(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Override
    public Set<Menu> listDeleteMenu(List<Menu> menuList, Set<Menu> menuSet) {
        // 递归找出待删除的菜单
        for (Menu menu : menuList) {
            menuSet.add(menu);
            LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Menu::getPid, menu.getMenuId());
            List<Menu> menus = menuMapper.selectList(wrapper);
            if (CollUtil.isNotEmpty(menus)) {
                listDeleteMenu(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public MenuDTO getById(Long id) {
        return super.getById(id);
    }

    @Override
    public List<MenuDTO> getSuperior(MenuDTO menuDto, List<Menu> menus) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        if (menuDto.getPid() == null) {
            wrapper.isNull(Menu::getPid).orderByAsc(Menu::getMenuSort);
            menus.addAll(menuMapper.selectList(wrapper));
            return ConvertUtils.convertList(menus, MenuDTO.class);
        }
        wrapper.eq(Menu::getPid, menuDto.getPid()).orderByAsc(Menu::getMenuSort);
        menus.addAll(menuMapper.selectList(wrapper));
        return getSuperior(getById(menuDto.getPid()), menus);
    }

    @Override
    public List<MenuDTO> buildTree(List<MenuDTO> menuDtos) {
        List<MenuDTO> trees = CollUtil.newArrayList();
        Set<Long> ids = CollUtil.newHashSet();
        for (MenuDTO menuDTO : menuDtos) {
            if (null == menuDTO.getPid()) {
                trees.add(menuDTO);
            }
            for (MenuDTO it : menuDtos) {
                if (it.getPid() != null && it.getPid().equals(menuDTO.getMenuId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(CollUtil.newArrayList());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getMenuId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getMenuId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<MenuVO> buildMenu(List<MenuDTO> menuDtos) {
        List<MenuVO> list = CollUtil.newLinkedList();
        menuDtos.forEach(menuDTO -> {
            if (menuDTO != null) {
                List<MenuDTO> menuDtoList = menuDTO.getChildren();
                MenuVO menuVo = new MenuVO();
                menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getTitle());
                // 一级目录需要加斜杠，不然会报警告
                menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() : menuDTO.getPath());
                menuVo.setHidden(menuDTO.getHidden());
                // 如果不是外链
                if (!menuDTO.getiFrame()) {
                    if (menuDTO.getPid() == null) {
                        menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                        // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                    } else if (menuDTO.getType() == 0) {
                        menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent()) ? "ParentView" : menuDTO.getComponent());
                    } else if (!StringUtils.isEmpty(menuDTO.getComponent())) {
                        menuVo.setComponent(menuDTO.getComponent());
                    }
                }
                menuVo.setMeta(new MenuMetaVO(menuDTO.getTitle(), menuDTO.getIcon(), !menuDTO.getCache()));
                if (CollectionUtil.isNotEmpty(menuDtoList)) {
                    menuVo.setAlwaysShow(true);
                    menuVo.setRedirect("noredirect");
                    menuVo.setChildren(buildMenu(menuDtoList));
                    // 处理是一级菜单并且没有子菜单的情况
                } else if (menuDTO.getPid() == null) {
                    MenuVO menuVo1 = new MenuVO();
                    menuVo1.setMeta(menuVo.getMeta());
                    // 非外链
                    if (!menuDTO.getiFrame()) {
                        menuVo1.setPath("index");
                        menuVo1.setName(menuVo.getName());
                        menuVo1.setComponent(menuVo.getComponent());
                    } else {
                        menuVo1.setPath(menuDTO.getPath());
                    }
                    menuVo.setName(null);
                    menuVo.setMeta(null);
                    menuVo.setComponent("Layout");
                    List<MenuVO> list1 = new ArrayList<>();
                    list1.add(menuVo1);
                    menuVo.setChildren(list1);
                }
                list.add(menuVo);
            }
        });
        return list;
    }

    @Override
    public void download(HttpServletResponse response, List<MenuDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (MenuDTO menu : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(14, true);
            map.put("上级菜单ID", menu.getPid());
            map.put("子菜单数目", menu.getSubCount());
            map.put("菜单类型", menu.getType());
            map.put("菜单标题", menu.getTitle());
            map.put("组件名称", menu.getComponentName());
            map.put("组件", menu.getComponent());
            map.put("排序", menu.getMenuSort());
            map.put("图标", menu.getIcon());
            map.put("链接地址", menu.getPath());
            map.put("是否外链", menu.getiFrame());
            map.put("缓存", menu.getCache());
            map.put("隐藏", menu.getHidden());
            map.put("权限", menu.getPermission());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 根据 name 查询
     *
     * @param title 菜单标题
     */
    private Menu getByTitle(String title) {
        return lambdaQuery().eq(Menu::getTitle, title).one();
    }

    /**
     * 根据 componentName 查询
     *
     * @param componentName 组件名称
     */
    private Menu getByComponentName(String componentName) {
        return lambdaQuery().eq(Menu::getComponentName, componentName).one();
    }

    /**
     * 更新子节点菜单数目
     *
     * @param menuId 菜单Id
     */
    private void updateSubCnt(Long menuId) {
        if (menuId != null) {
            int count = lambdaQuery().eq(Menu::getPid, menuId).count();

            LambdaUpdateWrapper<Menu> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(Menu::getMenuId, menuId);
            Menu menu = new Menu();
            menu.setSubCount(count);
            menuMapper.update(menu, updateWrapper);
        }
    }

    /**
     * 清理缓存
     *
     * @param id  菜单ID
     * @param pid 菜单父级ID
     */
    public void delCaches(Long id, Long pid) {
        List<User> users = userMapper.selectByMenuId(id);
        redisUtils.del(CacheConsts.MENU_ID + id);
        redisUtils.delByKeys(CacheConsts.MENU_USER, users.stream().map(User::getUserId).collect(Collectors.toSet()));
        redisUtils.del(CacheConsts.MENU_PID + (pid == null ? 0 : pid));
    }
}
