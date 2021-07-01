package org.utility.modules.system.rest;

import cn.hutool.core.lang.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.api.Result;
import org.utility.exception.BadRequestException;
import org.utility.modules.system.service.RoleService;
import org.utility.modules.system.service.dto.RoleDTO;
import org.utility.modules.system.service.dto.RoleQuery;
import org.utility.modules.system.service.dto.RoleSmallDTO;
import org.utility.util.SecurityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：角色管理")
@RestController
@RequestMapping(value = "/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "新增角色")
    @Log(value = "新增角色")
    @PreAuthorize(value = "@authorize.check('role:add')")
    @PostMapping
    public Result save(@Validated @RequestBody RoleDTO resource) {
        this.getLevels(resource.getLevel());
        roleService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除角色")
    @Log(value = "删除角色")
    @PreAuthorize(value = "@authorize.check('role:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            RoleDTO role = roleService.getById(id);
            this.getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改角色")
    @Log(value = "修改角色")
    @PreAuthorize(value = "@authorize.check('role:edit')")
    @PutMapping
    public Result update(@Validated @RequestBody RoleDTO resource) {
        this.getLevels(resource.getLevel());
        roleService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "修改角色菜单")
    @Log(value = "修改角色菜单")
    @PreAuthorize(value = "@authorize.check('role:edit')")
    @PutMapping(value = "/menu")
    public Result updateMenu(@RequestBody RoleDTO resource) {
        RoleDTO role = roleService.getById(resource.getRoleId());
        this.getLevels(role.getLevel());
        roleService.updateMenu(resource);
        return Result.success();
    }
   
    @ApiOperation(value = "查询角色")
    @PreAuthorize(value = "@authorize.check('role:list')")
    @GetMapping
    public Result page(RoleQuery query) {
        return Result.success(roleService.page(query));
    }

    @ApiOperation(value = "返回全部的角色")
    @PreAuthorize(value = "@authorize.check('user:add','user:edit','role:list')")
    @GetMapping(value = "/all")
    public Result list(){
        return Result.success(roleService.list(new RoleQuery()));
    }

    @ApiOperation(value = "获取单个角色")
    @PreAuthorize(value = "@authorize.check('role:list')")
    @GetMapping(value = "/{id}")
    public Result getOne(@PathVariable Long id){
        return Result.success(roleService.getById(id));
    }
    
    @ApiOperation(value = "获取用户级别")
    @GetMapping(value = "/level")
    public Result getLevel(){
        return Result.success(Dict.create().set("level", this.getLevels(null)));
    }
    
    @ApiOperation(value = "导出角色")
    @Log(value = "导出角色")
    @PreAuthorize(value = "@authorize.check('role:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, RoleQuery query) throws IOException {
        roleService.download(response, roleService.list(query));
    }
    
    /**
     * 获取用户的角色级别
     *
     * @return /
     */
    private int getLevels(Integer level) {
        List<Integer> levels =
                roleService.listByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
