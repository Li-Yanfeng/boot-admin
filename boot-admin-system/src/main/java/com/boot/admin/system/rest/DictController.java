package com.boot.admin.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.system.model.Dict;
import com.boot.admin.system.service.DictService;
import com.boot.admin.system.service.dto.DictDTO;
import com.boot.admin.system.service.dto.DictQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：字典管理")
@RestController
@RequestMapping(value = "/api/dicts")
@ResultWrapper
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @ApiOperation(value = "新增字典")
    @Log(value = "新增字典")
    @PreAuthorize(value = "@authorize.check('dicts:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Dict resource) {
        dictService.saveDict(resource);
    }

    @ApiOperation(value = "删除字典")
    @Log(value = "删除字典")
    @PreAuthorize(value = "@authorize.check('dicts:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        dictService.removeDictByIds(ids);
    }

    @ApiOperation(value = "修改字典")
    @Log(value = "修改字典")
    @PreAuthorize(value = "@authorize.check('dicts:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody Dict resource) {
        dictService.updateDictById(resource);
    }

    @ApiOperation(value = "查询字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping
    public Page<DictDTO> list(DictQuery query, Page<Dict> page) {
        return dictService.listDicts(query, page);
    }

    @ApiOperation(value = "查询字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping(value = "/lists")
    public List<DictDTO> list(DictQuery query) {
        return dictService.listDicts(query);
    }

    @ApiOperation(value = "导出字典")
    @Log(value = "导出字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, DictQuery query) {
        dictService.exportDict(dictService.listDicts(query), response);
    }
}
