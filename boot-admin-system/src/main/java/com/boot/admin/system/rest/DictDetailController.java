package com.boot.admin.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.core.validation.Update;
import com.boot.admin.system.model.DictDetail;
import com.boot.admin.system.service.DictDetailService;
import com.boot.admin.system.service.dto.DictDetailDTO;
import com.boot.admin.system.service.dto.DictDetailQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：字典详情管理")
@RestController
@RequestMapping(value = "/v1/dict_details")
@ResultWrapper
public class DictDetailController {

    private final DictDetailService dictDetailService;

    public DictDetailController(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    @ApiOperation(value = "新增字典详情")
    @Log(value = "新增字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody DictDetail resource) {
        dictDetailService.saveDictDetail(resource);
    }

    @ApiOperation(value = "删除字典详情")
    @Log(value = "删除字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        dictDetailService.removeDictDetailByIds(ids);
    }

    @ApiOperation(value = "修改字典详情")
    @Log(value = "修改字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody DictDetail resource) {
        dictDetailService.updateDictDetailById(resource);
    }

    @ApiOperation(value = "查询字典详情")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping
    public Page<DictDetailDTO> list(DictDetailQuery query, Page<DictDetail> page) {
        return dictDetailService.listDictDetails(query, page);
    }
}
