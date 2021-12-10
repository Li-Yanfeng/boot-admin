package org.utility.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.validation.Update;
import org.utility.system.model.DictDetail;
import org.utility.system.service.DictDetailService;
import org.utility.system.service.dto.DictDetailDTO;
import org.utility.system.service.dto.DictDetailQuery;

import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "数据字典详情管理")
@RestController
@RequestMapping(value = "/v1/dict_details")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    public DictDetailController(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    @ApiOperation(value = "新增数据字典详情")
    @Log(value = "新增数据字典详情")
    @PreAuthorize(value = "@authorize.check('dict_details:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody DictDetail resource) {
        dictDetailService.saveDictDetail(resource);
    }

    @ApiOperation(value = "删除数据字典详情")
    @Log(value = "删除数据字典详情")
    @PreAuthorize(value = "@authorize.check('dict_details:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        dictDetailService.removeDictDetailByIds(ids);
    }

    @ApiOperation(value = "修改数据字典详情")
    @Log(value = "修改数据字典详情")
    @PreAuthorize(value = "@authorize.check('dict_details:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody DictDetail resource) {
        dictDetailService.updateDictDetailById(resource);
    }

    @ApiOperation(value = "查询数据字典详情")
    @PreAuthorize(value = "@authorize.check('dict_details:list')")
    @GetMapping
    public Page<DictDetailDTO> list(DictDetailQuery query, Page<DictDetail> page) {
        return dictDetailService.listDictDetails(query, page);
    }
}
