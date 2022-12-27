package com.boot.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.model.ColumnConfig;
import com.boot.admin.model.TableInfo;
import com.boot.admin.service.ColumnConfigService;
import com.boot.admin.service.GenConfigService;
import com.boot.admin.service.TableInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Li Yanfeng
 */
@Tag(name = "系统：代码生成管理")
@RestController
@RequestMapping(value = "/generators")
@ResultWrapper
public class GeneratorController {

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    private final TableInfoService tableInfoService;
    private final ColumnConfigService columnConfigService;
    private final GenConfigService genConfigService;

    public GeneratorController(TableInfoService tableInfoService, ColumnConfigService columnConfigService,
                               GenConfigService genConfigService) {
        this.tableInfoService = tableInfoService;
        this.columnConfigService = columnConfigService;
        this.genConfigService = genConfigService;
    }

    @Operation(summary = "同步字段数据")
    @PostMapping(value = "/syncs")
    public void save(@RequestBody List<String> tables) {
        for (String table : tables) {
            columnConfigService.syncColumnConfig(columnConfigService.listColumnConfigs(table),
                columnConfigService.listColumnConfigs(table, true));
        }
    }

    @Operation(summary = "保存字段数据")
    @PutMapping
    public void update(@RequestBody List<ColumnConfig> columnConfigs) {
        columnConfigService.saveColumnConfig(columnConfigs);
    }

    @Operation(summary = "翻页查询表数据信息")
    @GetMapping(value = "/tables")
    public Page<TableInfo> listTables(String tableName, Page<TableInfo> page) {
        return tableInfoService.listTableInfos(tableName, page);
    }

    @Operation(summary = "查询表数据信息")
    @GetMapping(value = "/tables/lists")
    public List<TableInfo> listTables() {
        return tableInfoService.listTableInfos();
    }

    @Operation(summary = "查询字段数据")
    @GetMapping(value = "/columns")
    public Page<ColumnConfig> listColumns(String tableName, Page<ColumnConfig> page) {
        return columnConfigService.listColumnConfigs(tableName, page);
    }

    @Operation(summary = "生成代码")
    @PostMapping(value = "/{tableName}/{type}")
    public Object generator(@PathVariable String tableName, @PathVariable Integer type, HttpServletRequest request,
                            HttpServletResponse response) {
        if (!generatorEnabled && type == 0) {
            throw new BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！");
        }
        switch (type) {
            // 生成代码
            case 0:
                columnConfigService.generator(genConfigService.getGenConfigByTableName(tableName),
                    columnConfigService.listColumnConfigs(tableName));
                break;
            // 预览
            case 1:
                return columnConfigService.preview(genConfigService.getGenConfigByTableName(tableName),
                    columnConfigService.listColumnConfigs(tableName));
            // 打包
            case 2:
                columnConfigService.download(genConfigService.getGenConfigByTableName(tableName),
                    columnConfigService.listColumnConfigs(tableName), request, response);
                break;
            default:
                throw new BadRequestException("没有这个选项");
        }
        return null;
    }
}
