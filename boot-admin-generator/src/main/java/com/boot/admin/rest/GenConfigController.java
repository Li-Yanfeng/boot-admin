package com.boot.admin.rest;

import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.core.validation.Update;
import com.boot.admin.model.GenConfig;
import com.boot.admin.service.GenConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Li Yanfeng
 */
@Api(tags = "系统：代码生成器配置管理")
@RestController
@RequestMapping(value = "/v1/gen_configs")
public class GenConfigController {

    private final GenConfigService genConfigService;

    public GenConfigController(GenConfigService genConfigService) {
        this.genConfigService = genConfigService;
    }

    @ApiOperation(value = "修改代码生成配置")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody GenConfig resource) {
        genConfigService.saveGenConfig(resource);
    }

    @ApiOperation(value = "查询代码生成配置")
    @GetMapping(value = "/{tableName}")
    public GenConfig getByTableName(@PathVariable String tableName) {
        return genConfigService.getGenConfigByTableName(tableName);
    }
}
