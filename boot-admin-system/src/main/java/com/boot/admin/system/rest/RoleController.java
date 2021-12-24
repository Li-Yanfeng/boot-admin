package com.boot.admin.system.rest;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.core.validation.Update;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.system.model.Role;
import com.boot.admin.system.service.RoleService;
import com.boot.admin.system.service.dto.RoleDTO;
import com.boot.admin.system.service.dto.RoleQuery;
import com.boot.admin.system.service.dto.RoleSmallDTO;
import com.boot.admin.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：角色管理")
@RestController
@RequestMapping(value = "/v1/roles")
@ResultWrapper
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "新增角色")
    @Log(value = "新增角色")
    @PreAuthorize(value = "@authorize.check('roles:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody RoleDTO resource) {
        getRoleLevelByCurrentUser(resource.getLevel());
        roleService.saveRole(resource);
    }

    @ApiOperation(value = "删除角色")
    @Log(value = "删除角色")
    @PreAuthorize(value = "@authorize.check('roles:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            RoleDTO role = roleService.getRoleById(id);
            getRoleLevelByCurrentUser(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.removeRoleByIds(ids);
    }

    @ApiOperation(value = "修改角色")
    @Log(value = "修改角色")
    @PreAuthorize(value = "@authorize.check('roles:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody RoleDTO resource) {
        getRoleLevelByCurrentUser(resource.getLevel());
        roleService.updateRoleById(resource);
    }

    @ApiOperation(value = "修改角色菜单")
    @Log(value = "修改角色菜单")
    @PreAuthorize(value = "@authorize.check('roles:edit')")
    @NoRepeatSubmit
    @PutMapping(value = "/menus")
    public void updateMenu(@Validated(value = Update.class) @RequestBody RoleDTO resource) {
        RoleDTO role = roleService.getRoleById(resource.getRoleId());
        getRoleLevelByCurrentUser(role.getLevel());
        roleService.updateMenu(resource, role);
    }

    @ApiOperation(value = "查询角色")
    @PreAuthorize(value = "@authorize.check('roles:list')")
    @GetMapping
    public Page<RoleDTO> list(RoleQuery query, Page<Role> page) {
        return roleService.listRoles(query, page);
    }

    @ApiOperation(value = "返回全部的角色")
    @PreAuthorize(value = "@authorize.check('roles:list','users:add','users:edit')")
    @GetMapping(value = "/lists")
    public List<RoleDTO> listAll(RoleQuery query) {
        return roleService.listRoles(query);
    }

    @ApiOperation(value = "获取单个角色")
    @PreAuthorize(value = "@authorize.check('roles:list')")
    @GetMapping(value = "/{id}")
    public RoleDTO info(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @ApiOperation(value = "获取用户级别")
    @GetMapping(value = "/levels")
    public Dict getLevel() {
        return Dict.create().set("level", getRoleLevelByCurrentUser(null));
    }

    @ApiOperation(value = "导出角色")
    @Log(value = "导出角色")
    @PreAuthorize(value = "@authorize.check('roles:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, RoleQuery query) throws IOException {
        roleService.exportRole(roleService.listRoles(query), response);
    }

    /**
     * 获取当前用户的角色级别
     *
     * @return /
     */
    private int getRoleLevelByCurrentUser(Integer level) {
        List<Integer> levels =
            roleService.listRolesByUserId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
