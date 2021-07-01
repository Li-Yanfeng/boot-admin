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
import org.utility.modules.system.model.Dept;
import org.utility.modules.system.service.DeptService;
import org.utility.modules.system.service.dto.DeptDTO;
import org.utility.modules.system.service.dto.DeptQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：部门管理")
@RestController
@RequestMapping(value = "/api/dept")
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @ApiOperation(value = "新增部门")
    @Log(value = "新增部门")
    @PreAuthorize(value = "@authorize.check('dept:add')")
    @PostMapping
    public Result save(@Validated @RequestBody Dept resource) {
        deptService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除部门")
    @Log(value = "删除部门")
    @PreAuthorize(value = "@authorize.check('dept:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        Set<Long> deptIds = CollUtil.newHashSet();
        for (Long id : ids) {
            List<Dept> deptList = deptService.listByPid(id);
            deptIds.add(deptService.getById(id).getDeptId());
            if (CollectionUtil.isNotEmpty(deptList)) {
                deptIds = deptService.listDeleteDept(deptList, deptIds);
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptIds);
        deptService.removeByIds(deptIds);
        return Result.success();
    }

    @ApiOperation(value = "修改部门")
    @Log(value = "修改部门")
    @PreAuthorize(value = "@authorize.check('dept:edit')")
    @PutMapping
    public Result update(@Validated @RequestBody Dept resource) {
        deptService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询部门")
    @PreAuthorize(value = "@authorize.check('user:list','dept:list')")
    @GetMapping
    public Result page(DeptQuery query) {
        return Result.success(deptService.page(query));
    }

    @ApiOperation(value = "查询部门:根据ID获取同级与上级数据")
    @PreAuthorize(value = "@authorize.check('user:list','dept:list')")
    @PostMapping("/superior")
    public Result getSuperior(@RequestBody List<Long> ids) {
        Set<DeptDTO> deptDtos = CollUtil.newLinkedHashSet();
        for (Long id : ids) {
            DeptDTO deptDto = deptService.getById(id);
            List<DeptDTO> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            deptDtos.addAll(depts);
        }
        return Result.success(deptService.buildTree(new ArrayList<>(deptDtos)));
    }

    @ApiOperation(value = "导出部门")
    @Log(value = "导出部门")
    @PreAuthorize(value = "@authorize.check('dept:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, DeptQuery query) throws IOException {
        deptService.download(response, deptService.list(query));
    }
}
