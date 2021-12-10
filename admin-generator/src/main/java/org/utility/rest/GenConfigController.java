package org.utility.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.validation.Update;
import org.utility.model.GenConfig;
import org.utility.service.GenConfigService;

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
    @PreAuthorize(value = "@authorize.check('gen_configs:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody GenConfig resource) {
        genConfigService.saveGenConfig(resource);
    }

    @ApiOperation(value = "查询代码生成配置")
    @PreAuthorize(value = "@authorize.check('gen_configs:list')")
    @GetMapping(value = "/{tableName}")
    public GenConfig getByTableName(@PathVariable String tableName) {
        return genConfigService.getGenConfigByTableName(tableName);
    }
}
