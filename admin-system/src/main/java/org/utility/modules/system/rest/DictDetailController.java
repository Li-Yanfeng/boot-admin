package org.utility.modules.system.rest;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.api.Result;
import org.utility.modules.system.model.DictDetail;
import org.utility.modules.system.service.DictDetailService;
import org.utility.modules.system.service.dto.DictDetailDTO;
import org.utility.modules.system.service.dto.DictDetailQuery;

import java.util.List;
import java.util.Map;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：数据字典详情管理")
@RestController
@RequestMapping(value = "/api/dictDetail")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    public DictDetailController(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    @ApiOperation(value = "新增数据字典详情")
    @Log(value = "新增数据字典详情")
    @PreAuthorize(value = "@authorize.check('dict:add')")
    @NoRepeatSubmit
    @PostMapping
    public Result save(@Validated @RequestBody DictDetail resource) {
        dictDetailService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除数据字典详情")
    @Log(value = "删除数据字典详情")
    @PreAuthorize(value = "@authorize.check('dict:del')")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        dictDetailService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "修改数据字典详情")
    @Log(value = "修改数据字典详情")
    @PreAuthorize(value = "@authorize.check('dict:edit')")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody DictDetail resource) {
        dictDetailService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询数据字典详情")
    @GetMapping
    public Result page(@PageableDefault(sort = {"dictSort"}, direction = Sort.Direction.ASC) DictDetailQuery query) {
        if (StrUtil.isBlank(query.getDictName())) {
            return Result.success(dictDetailService.list(query));
        } else {
            return Result.success(dictDetailService.listByDictName(query.getDictName()));
        }
    }

    @ApiOperation(value = "查询多个字典详情")
    @GetMapping(value = "/map")
    public Result dictDetailMaps(@RequestParam String dictName) {
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetailDTO>> dictMap = MapUtil.newHashMap(5);
        for (String name : names) {
            dictMap.put(name, dictDetailService.listByDictName(name));
        }
        return Result.success(dictMap);
    }
}
