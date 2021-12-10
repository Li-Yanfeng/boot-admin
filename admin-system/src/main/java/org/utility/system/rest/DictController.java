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
import org.utility.system.model.Dict;
import org.utility.system.service.DictService;
import org.utility.system.service.dto.DictDTO;
import org.utility.system.service.dto.DictQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "数据字典管理")
@RestController
@RequestMapping(value = "/v1/dicts")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @ApiOperation(value = "新增数据字典")
    @Log(value = "新增数据字典")
    @PreAuthorize(value = "@authorize.check('dicts:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Dict resource) {
        dictService.saveDict(resource);
    }

    @ApiOperation(value = "删除数据字典")
    @Log(value = "删除数据字典")
    @PreAuthorize(value = "@authorize.check('dicts:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        dictService.removeDictByIds(ids);
    }

    @ApiOperation(value = "修改数据字典")
    @Log(value = "修改数据字典")
    @PreAuthorize(value = "@authorize.check('dicts:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody Dict resource) {
        dictService.updateDictById(resource);
    }

    @ApiOperation(value = "查询数据字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping
    public Page<DictDTO> list(DictQuery query, Page<Dict> page) {
        return dictService.listDicts(query, page);
    }

    @ApiOperation(value = "查询数据字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping(value = "/list")
    public List<DictDTO> list(DictQuery query) {
        return dictService.listDicts(query);
    }

    @ApiOperation(value = "导出数据字典")
    @Log(value = "导出数据字典")
    @PreAuthorize(value = "@authorize.check('dicts:list')")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, DictQuery query) throws IOException {
        dictService.exportDict(dictService.listDicts(query), response);
    }
}
