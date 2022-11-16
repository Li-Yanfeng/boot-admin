package com.boot.admin.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.system.model.DictDetail;
import com.boot.admin.system.service.DictDetailService;
import com.boot.admin.system.service.dto.DictDetailDTO;
import com.boot.admin.system.service.dto.DictDetailQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Tag(name = "系统：字典详情管理")
@RestController
@RequestMapping(value = "/api/dict_details")
@ResultWrapper
public class DictDetailController {

    private final DictDetailService dictDetailService;

    public DictDetailController(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    @Operation(summary = "新增字典详情")
    @Log(value = "新增字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody DictDetail resource) {
        dictDetailService.saveDictDetail(resource);
    }

    @Operation(summary = "删除字典详情")
    @Log(value = "删除字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        dictDetailService.removeDictDetailByIds(ids);
    }

    @Operation(summary = "修改字典详情")
    @Log(value = "修改字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody DictDetail resource) {
        dictDetailService.updateDictDetailById(resource);
    }

    @Operation(summary = "查询字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping
    public Page<DictDetailDTO> list(DictDetailQuery query, Page<DictDetail> page) {
        return dictDetailService.listDictDetails(query, page);
    }
}
