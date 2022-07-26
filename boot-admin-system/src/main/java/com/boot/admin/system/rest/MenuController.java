package com.boot.admin.system.rest;

import cn.hutool.core.collection.CollUtil;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.system.model.Menu;
import com.boot.admin.system.model.vo.MenuVO;
import com.boot.admin.system.service.MenuService;
import com.boot.admin.system.service.dto.MenuDTO;
import com.boot.admin.system.service.dto.MenuQuery;
import com.boot.admin.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：菜单管理")
@RestController
@RequestMapping(value = "/api/menus")
@ResultWrapper
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation(value = "新增菜单")
    @Log(value = "新增菜单")
    @PreAuthorize(value = "@authorize.check('menus:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Menu resource) {
        menuService.saveMenu(resource);
    }

    @ApiOperation(value = "删除菜单")
    @Log(value = "删除菜单")
    @PreAuthorize(value = "@authorize.check('menus:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        List<MenuDTO> menus = CollUtil.newArrayList();
        for (Long id : ids) {
            List<MenuDTO> menuList = menuService.listMenus(new MenuQuery(id));
            menus.add(menuService.getMenuById(id));
            menus = menuService.listMenusChildren(menuList, menus);
        }
        menuService.removeMenu(menus);
    }

    @ApiOperation(value = "修改菜单")
    @Log(value = "修改菜单")
    @PreAuthorize(value = "@authorize.check('menus:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody Menu resource) {
        menuService.updateMenuById(resource);
    }

    @ApiOperation(value = "查询菜单")
    @PreAuthorize(value = "@authorize.check('menus:list')")
    @GetMapping
    public List<MenuDTO> list(MenuQuery query) {
        return menuService.listMenus(query);
    }

    @ApiOperation(value = "返回全部的菜单")
    @PreAuthorize(value = "@authorize.check('menus:list','roles:list')")
    @GetMapping(value = "/lazies")
    public List<MenuDTO> listAll(MenuQuery query) {
        return menuService.listMenus(query);
    }

    @ApiOperation(value = "获取前端所需菜单")
    @GetMapping(value = "/builds")
    public List<MenuVO> buildMenus() {
        List<MenuDTO> menus = menuService.listMenusByUserId(SecurityUtils.getCurrentUserId());
        menus = menuService.buildTree(menus);
        return menuService.buildMenus(menus);
    }

    @ApiOperation(value = "根据菜单ID返回所有子节点ID，包含自身ID")
    @PreAuthorize(value = "@authorize.check('menus:list','roles:list')")
    @GetMapping(value = "/children")
    public Set<Long> child(Long id) {
        List<MenuDTO> menus = CollUtil.newArrayList();
        List<MenuDTO> menuList = menuService.listMenus(new MenuQuery(id));
        menus.add(menuService.getMenuById(id));
        menus = menuService.listMenusChildren(menuList, menus);
        return menus.stream().map(MenuDTO::getMenuId).collect(Collectors.toSet());
    }

    @ApiOperation(value = "查询菜单:根据ID获取同级与上级数据")
    @PreAuthorize(value = "@authorize.check('menu:list')")
    @GetMapping(value = "/superiors")
    public List<MenuDTO> getSuperior(@RequestBody List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            List<MenuDTO> menus = CollUtil.newArrayList();
            for (Long id : ids) {
                MenuDTO menu = menuService.getMenuById(id);
                menus.addAll(menuService.listMenusSuperior(menu, CollUtil.newArrayList()));
            }
            return menuService.buildTree(menus);
        }
        return menuService.listMenus(new MenuQuery(CommonConstant.TOP_ID));
    }

    @ApiOperation(value = "导出菜单")
    @Log(value = "导出菜单")
    @PreAuthorize(value = "@authorize.check('menus:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, MenuQuery query) {
        menuService.exportMenu(menuService.listMenus(query), response);
    }
}
