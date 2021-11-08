package org.utility.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.api.Result;
import org.utility.modules.system.model.Dict;
import org.utility.modules.system.service.DictService;
import org.utility.modules.system.service.dto.DictQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：数据字典管理")
@RestController
@RequestMapping(value = "/api/dict")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @ApiOperation(value = "新增数据字典")
    @Log(value = "新增数据字典")
    @PreAuthorize(value = "@authorize.check('dict:add')")
    @NoRepeatSubmit
    @PostMapping
    public Result save(@Validated @RequestBody Dict resource) {
        dictService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除数据字典")
    @Log(value = "删除数据字典")
    @PreAuthorize(value = "@authorize.check('dict:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        dictService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改数据字典")
    @Log(value = "修改数据字典")
    @PreAuthorize(value = "@authorize.check('dict:edit')")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody Dict resource) {
        dictService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询数据字典")
    @PreAuthorize(value = "@authorize.check('dict:list')")
    @GetMapping
    public Result page(DictQuery query) {
        return Result.success(dictService.page(query));
    }

    @ApiOperation(value = "查询数据字典")
    @PreAuthorize(value = "@authorize.check('dict:list')")
    @GetMapping(value = "/list")
    public Result list(DictQuery query) {
        return Result.success(dictService.list(query));
    }

    @ApiOperation(value = "导出数据字典")
    @Log(value = "导出数据字典")
    @PreAuthorize(value = "@authorize.check('dict:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, DictQuery query) throws IOException {
        dictService.download(response, dictService.list(query));
    }
}
