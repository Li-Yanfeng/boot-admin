package com.boot.admin.system.rest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.core.validation.Update;
import com.boot.admin.system.model.Dept;
import com.boot.admin.system.service.DeptService;
import com.boot.admin.system.service.dto.DeptDTO;
import com.boot.admin.system.service.dto.DeptQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：部门管理")
@RestController
@RequestMapping(value = "/api/depts")
@ResultWrapper
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @ApiOperation(value = "新增部门")
    @Log(value = "新增部门")
    @PreAuthorize(value = "@authorize.check('depts:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Dept resource) {
        deptService.saveDept(resource);
    }

    @ApiOperation(value = "删除部门")
    @Log(value = "删除部门")
    @PreAuthorize(value = "@authorize.check('depts:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        Set<DeptDTO> depts = CollUtil.newHashSet();
        for (Long id : ids) {
            depts.addAll(deptService.listDeptsChildren(id));
        }
        // 验证是否被关联
        Set<Long> deptIds = depts.stream().map(DeptDTO::getDeptId).collect(Collectors.toSet());
        deptService.verification(deptIds);
        deptService.removeDept(depts);
    }

    @ApiOperation(value = "修改部门")
    @Log(value = "修改部门")
    @PreAuthorize(value = "@authorize.check('depts:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody Dept resource) {
        deptService.updateDeptById(resource);
    }

    @ApiOperation(value = "查询部门")
    @PreAuthorize(value = "@authorize.check('users:list','depts:list')")
    @GetMapping
    public List<Tree<Long>> list(DeptQuery query) {
        return deptService.buildTree(deptService.listDepts(query));
    }

    @ApiOperation(value = "查询部门:根据ID获取同级与上级数据")
    @PreAuthorize(value = "@authorize.check('users:list','depts:list')")
    @GetMapping(value = "/superiors")
    public List<Tree<Long>> getSuperior(@RequestBody List<Long> ids) {
        Set<DeptDTO> depts = CollUtil.newHashSet();
        for (Long id : ids) {
            depts.add(deptService.getDeptById(id));
            depts.addAll(deptService.listDeptsSuperior(id));
        }
        return deptService.buildTree(depts);
    }

    @ApiOperation(value = "导出部门")
    @Log(value = "导出部门")
    @PreAuthorize(value = "@authorize.check('depts:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, DeptQuery query) throws IOException {
        deptService.exportDept(deptService.listDepts(query), response);
    }
}
