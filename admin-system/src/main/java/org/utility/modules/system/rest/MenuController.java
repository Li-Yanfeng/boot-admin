package org.utility.modules.system.rest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.api.Result;
import org.utility.modules.system.model.Menu;
import org.utility.modules.system.service.MenuService;
import org.utility.modules.system.service.dto.MenuDTO;
import org.utility.modules.system.service.dto.MenuQuery;
import org.utility.util.ConvertUtils;
import org.utility.util.SecurityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：菜单管理")
@RestController
@RequestMapping(value = "/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation(value = "新增菜单")
    @Log(value = "新增菜单")
    @PreAuthorize(value = "@authorize.check('menu:add')")
    @PostMapping
    public Result save(@Validated @RequestBody Menu resource) {
        menuService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除菜单")
    @Log(value = "删除菜单")
    @PreAuthorize(value = "@authorize.check('menu:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        Set<Menu> menuSet = CollectionUtil.newHashSet();
        for (Long id : ids) {
            List<MenuDTO> menuList = menuService.listByPid(id);
            menuList.add(menuService.getById(id));
            menuSet = menuService.listDeleteMenu(ConvertUtils.convertList(menuList, Menu.class), menuSet);
        }
        menuService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改菜单")
    @Log(value = "修改菜单")
    @PreAuthorize(value = "@authorize.check('menu:edit')")
    @PutMapping
    public Result update(@Validated @RequestBody Menu resource) {
        menuService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询菜单")
    @PreAuthorize(value = "@authorize.check('menu:list')")
    @GetMapping
    public Result list(MenuQuery query) {
        return Result.success(menuService.list(query));
    }

    @ApiOperation(value = "返回全部的菜单")
    @PreAuthorize(value = "@authorize.check('role:list', 'menu:list')")
    @GetMapping(value = "/lazy")
    public Result listAll(@RequestParam Long pid) {
        return Result.success(menuService.listByPid(pid));
    }

    @ApiOperation(value = "获取前端所需菜单")
    @GetMapping(value = "/build")
    public Result buildMenus() {
        List<MenuDTO> menuDtoList = menuService.listByUserId(SecurityUtils.getCurrentUserId());
        List<MenuDTO> menuDtos = menuService.buildTree(menuDtoList);
        return Result.success(menuService.buildMenu(menuDtos));
    }

    @ApiOperation(value = "根据菜单ID返回所有子节点ID，包含自身ID")
    @PreAuthorize(value = "@authorize.check('role:list', 'menu:list')")
    @GetMapping(value = "/child")
    public Result child(@RequestParam Long id) {
        Set<Menu> menuSet = CollUtil.newHashSet();
        List<MenuDTO> menuList = menuService.listByPid(id);
        menuList.add(menuService.getById(id));
        menuSet = menuService.listChildMenu(ConvertUtils.convertList(menuList, Menu.class), menuSet);
        Set<Long> ids = menuSet.stream().map(Menu::getMenuId).collect(Collectors.toSet());
        return Result.success(ids);
    }

    @ApiOperation(value = "查询菜单:根据ID获取同级与上级数据")
    @PreAuthorize(value = "@authorize.check('menu:list')")
    @PostMapping("/superior")
    public Result getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDTO> menuDtos = CollUtil.newLinkedHashSet();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                MenuDTO menuDto = menuService.getById(id);
                menuDtos.addAll(menuService.getSuperior(menuDto, CollUtil.newArrayList()));
            }
            return Result.success(menuService.buildTree(CollUtil.newArrayList(menuDtos)));
        }
        return Result.success(menuService.listByPid(null));
    }

    @ApiOperation(value = "导出菜单")
    @Log(value = "导出菜单")
    @PreAuthorize(value = "@authorize.check('menu:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, MenuQuery query) throws IOException {
        menuService.download(response, menuService.list(query));
    }
}
