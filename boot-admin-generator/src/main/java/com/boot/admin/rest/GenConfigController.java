package com.boot.admin.rest;

import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.model.GenConfig;
import com.boot.admin.service.GenConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Li Yanfeng
 */
@Tag(name = "系统：代码生成器配置管理")
@RestController
@RequestMapping(value = "/gen_configs")
public class GenConfigController {

    private final GenConfigService genConfigService;

    public GenConfigController(GenConfigService genConfigService) {
        this.genConfigService = genConfigService;
    }

    @Operation(summary = "修改代码生成配置")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody GenConfig resource) {
        genConfigService.saveGenConfig(resource);
    }

    @Operation(summary = "查询代码生成配置")
    @GetMapping(value = "/{tableName}")
    public GenConfig getByTableName(@PathVariable String tableName) {
        return genConfigService.getGenConfigByTableName(tableName);
    }
}
