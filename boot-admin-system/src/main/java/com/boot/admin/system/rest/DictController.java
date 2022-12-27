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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Tag(name = "系统：字典管理")
@RestController
@RequestMapping(value = "/dicts")
@ResultWrapper
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @Operation(summary = "新增字典")
    @Log(value = "新增字典")
    @PreAuthorize(value = "@authorize.check('dicts:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Dict resource) {
        dictService.saveDict(resource);
    }

    @Operation(summary = "删除字典")
    @Log(value = "删除字典")
    @PreAuthorize(value = "@authorize.check('dicts:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        dictService.removeDictByIds(ids);
    }

    @Operation(summary = "修改字典")
    @Log(value = "修改字典")
    @PreAuthorize(value = "@authorize.check('dicts:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody Dict resource) {
        dictService.updateDictById(resource);
    }

    @Operation(summary = "查询字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping
    public Page<DictDTO> list(DictQuery query, Page<Dict> page) {
        return dictService.listDicts(query, page);
    }

    @Operation(summary = "查询字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping(value = "/lists")
    public List<DictDTO> list(DictQuery query) {
        return dictService.listDicts(query);
    }

    @Operation(summary = "导出字典")
    @Log(value = "导出字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, DictQuery query) {
        dictService.exportDict(dictService.listDicts(query), response);
    }
}
