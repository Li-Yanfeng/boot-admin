package org.utility.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.api.Result;
import org.utility.exception.BadRequestException;
import org.utility.modules.mnt.model.Database;
import org.utility.modules.mnt.service.DatabaseService;
import org.utility.modules.mnt.service.dto.DatabaseDTO;
import org.utility.modules.mnt.service.dto.DatabaseQuery;
import org.utility.modules.mnt.util.SqlUtils;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Api(tags = "运维：数据库管理")
@RestController
@RequestMapping(value = "/api/database")
public class DatabaseController {

    private final String fileSavePath = FileUtils.getTmpDirPath() + "/";

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @ApiOperation(value = "新增数据库")
    @Log(value = "新增数据库")
    @PreAuthorize(value = "@authorize.check('database:add')")
    @NoRepeatSubmit
    @PostMapping
    public Result save(@Validated @RequestBody Database resource) {
        databaseService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除数据库")
    @Log(value = "删除数据库")
    @PreAuthorize(value = "@authorize.check('database:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        databaseService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改数据库")
    @Log(value = "修改数据库")
    @PreAuthorize(value = "@authorize.check('database:edit')")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody Database resource) {
        databaseService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询数据库")
    @PreAuthorize(value = "@authorize.check('database:list')")
    @GetMapping
    public Result page(DatabaseQuery query) {
        return Result.success(databaseService.page(query));
    }

    @ApiOperation(value = "测试数据库链接")
    @Log(value = "测试数据库链接")
    @PreAuthorize(value = "@authorize.check('database:testConnect')")
    @NoRepeatSubmit
    @PostMapping("/testConnect")
    public Result testConnect(@Validated @RequestBody Database resources) {
        return Result.success(databaseService.testConnection(resources));
    }

    @ApiOperation(value = "执行SQL脚本")
    @Log(value = "执行SQL脚本")
    @PreAuthorize(value = "@authorize.check('database:add')")
    @NoRepeatSubmit
    @PostMapping(value = "/upload")
    public Result upload(@RequestBody MultipartFile file, HttpServletRequest request, Long id) throws Exception {
        DatabaseDTO database = databaseService.getById(id);
        String fileName;
        if (database != null) {
            fileName = file.getOriginalFilename();
            File executeFile = new File(fileSavePath + fileName);
            FileUtils.del(executeFile);
            file.transferTo(executeFile);
            String result = SqlUtils.executeFile(database.getJdbcUrl(), database.getUserName(), database.getPwd(),
                    executeFile);
            return Result.success(result);
        } else {
            throw new BadRequestException("Database not exist");
        }
    }

    @ApiOperation(value = "导出数据库")
    @Log(value = "导出数据库")
    @PreAuthorize(value = "@authorize.check('database:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, DatabaseQuery query) throws IOException {
        databaseService.download(response, databaseService.list(query));
    }
}
