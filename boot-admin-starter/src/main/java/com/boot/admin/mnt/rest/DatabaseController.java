package com.boot.admin.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.core.validation.Update;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mnt.model.Database;
import com.boot.admin.mnt.service.DatabaseService;
import com.boot.admin.mnt.service.dto.DatabaseDTO;
import com.boot.admin.mnt.service.dto.DatabaseQuery;
import com.boot.admin.mnt.util.SqlUtils;
import com.boot.admin.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "运维：数据库管理")
@RestController
@RequestMapping(value = "/api/databases")
@ResultWrapper
public class DatabaseController {

    private final String fileSavePath = FileUtils.getTmpDirPath() + "/";

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @ApiOperation(value = "新增数据库")
    @Log(value = "新增数据库")
    @PreAuthorize(value = "@authorize.check('databases:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Database resource) {
        databaseService.saveDatabase(resource);
    }

    @ApiOperation(value = "删除数据库")
    @Log(value = "删除数据库")
    @PreAuthorize(value = "@authorize.check('databases:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        databaseService.removeDatabaseByIds(ids);
    }

    @ApiOperation(value = "修改数据库")
    @Log(value = "修改数据库")
    @PreAuthorize(value = "@authorize.check('databases:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody Database resource) {
        databaseService.updateDatabaseById(resource);
    }

    @ApiOperation(value = "查询数据库")
    @PreAuthorize(value = "@authorize.check('databases:list')")
    @GetMapping
    public Page<DatabaseDTO> list(DatabaseQuery query, Page<Database> page) {
        return databaseService.listDatabases(query, page);
    }

    @ApiOperation(value = "测试数据库链接")
    @Log(value = "测试数据库链接")
    @PreAuthorize(value = "@authorize.check('database:testConnect')")
    @NoRepeatSubmit
    @PostMapping(value = "/test_connect")
    public boolean testConnect(@Validated @RequestBody Database resources) {
        return databaseService.testConnection(resources);
    }

    @ApiOperation(value = "执行 SQL 脚本")
    @Log(value = "执行 SQL 脚本")
    @PreAuthorize(value = "@authorize.check('database:add')")
    @NoRepeatSubmit
    @PostMapping(value = "/uploads")
    public String upload(@RequestBody MultipartFile file, HttpServletRequest request, Long id) throws Exception {
        DatabaseDTO database = databaseService.getDatabaseById(id);
        String fileName;
        if (database != null) {
            fileName = file.getOriginalFilename();
            File executeFile = new File(fileSavePath + fileName);
            FileUtils.del(executeFile);
            file.transferTo(executeFile);
            return SqlUtils.executeFile(database.getJdbcUrl(), database.getUserName(), database.getPwd(), executeFile);
        } else {
            throw new BadRequestException("Database not exist");
        }
    }

    @ApiOperation(value = "导出数据库")
    @Log(value = "导出数据库")
    @PreAuthorize(value = "@authorize.check('databases:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, DatabaseQuery query) throws IOException {
        databaseService.exportDatabase(databaseService.listDatabases(query), response);
    }
}
