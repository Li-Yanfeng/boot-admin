package org.utility.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.utility.exception.BadRequestException;
import org.utility.model.ColumnConfig;
import org.utility.model.TableInfo;
import org.utility.service.ColumnConfigService;
import org.utility.service.GenConfigService;
import org.utility.service.TableInfoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Li Yanfeng
 */
@Api(tags = "系统：代码生成管理")
@RestController
@RequestMapping(value = "/v1/generator")
public class GeneratorController {

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    private final TableInfoService tableInfoService;
    private final ColumnConfigService columnConfigService;
    private final GenConfigService genConfigService;

    public GeneratorController(TableInfoService tableInfoService, ColumnConfigService columnConfigService, GenConfigService genConfigService) {
        this.tableInfoService = tableInfoService;
        this.columnConfigService = columnConfigService;
        this.genConfigService = genConfigService;
    }

    @ApiOperation("同步字段数据")
    @PostMapping(value = "sync")
    public void save(@RequestBody List<String> tables) {
        for (String table : tables) {
            columnConfigService.syncColumnConfig(columnConfigService.listColumnConfigs(table), columnConfigService.listColumnConfigs(table, true));
        }
    }

    @ApiOperation(value = "保存字段数据")
    @PutMapping
    public void update(@RequestBody List<ColumnConfig> columnConfigs) {
        columnConfigService.saveColumnConfig(columnConfigs);
    }

    @ApiOperation(value = "翻页查询表数据信息")
    @GetMapping(value = "/tables")
    public Page<TableInfo> listTables(String tableName, Page<TableInfo> page) {
        return tableInfoService.listTableInfos(tableName, page);
    }

    @ApiOperation(value = "查询表数据信息")
    @GetMapping(value = "/tables/all")
    public List<TableInfo> listTables() {
        return tableInfoService.listTableInfos();
    }

    @ApiOperation(value = "查询字段数据")
    @GetMapping(value = "/columns")
    public Page<ColumnConfig> listColumns(String tableName, Page<ColumnConfig> page) {
        return columnConfigService.listColumnConfigs(tableName, page);
    }

    @ApiOperation("生成代码")
    @PostMapping(value = "/{tableName}/{type}")
    public Object generator(@PathVariable String tableName, @PathVariable Integer type, HttpServletRequest request, HttpServletResponse response) {
        if (!generatorEnabled && type == 0) {
            throw new BadRequestException("此环境不允许生成代码，请选择预览或者下载查看！");
        }
        switch (type) {
            // 生成代码
            case 0:
                columnConfigService.generator(genConfigService.getGenConfigByTableName(tableName), columnConfigService.listColumnConfigs(tableName));
                break;
            // 预览
            case 1:
                return columnConfigService.preview(genConfigService.getGenConfigByTableName(tableName), columnConfigService.listColumnConfigs(tableName));
            // 打包
            case 2:
                columnConfigService.download(genConfigService.getGenConfigByTableName(tableName), columnConfigService.listColumnConfigs(tableName), request, response);
                break;
            default:
                throw new BadRequestException("没有这个选项");
        }
        return null;
    }
}
