package org.utility.rest;

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
import org.utility.exception.enums.UserErrorCode;
import org.utility.model.LocalStorage;
import org.utility.service.LocalStorageService;
import org.utility.service.dto.LocalStorageQuery;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Api(tags = "工具：本地存储管理")
@RestController
@RequestMapping(value = "/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;

    public LocalStorageController(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
    }

    @ApiOperation(value = "上传文件")
    @PreAuthorize(value = "@authorize.check('storage:add')")
    @NoRepeatSubmit
    @PostMapping
    public Result uploadFile(@RequestParam String name, @RequestParam("file") MultipartFile file) {
        localStorageService.save(name, file);
        return Result.success();
    }

    @ApiOperation(value = "上传图片")
    @NoRepeatSubmit
    @PostMapping(value = "/pictures")
    public Result uploadPicture(@RequestParam MultipartFile file) {
        // 判断文件是否为图片
        String suffix = FileUtils.getExtensionName(file.getOriginalFilename());
        if (!FileUtils.IMAGE.equals(FileUtils.getFileType(suffix))) {
            throw new BadRequestException(UserErrorCode.USER_UPLOAD_FILE_TYPE_DOES_NOT_MATCH);
        }
        LocalStorage localStorage = localStorageService.save(null, file);
        return Result.success(localStorage);
    }

    @ApiOperation(value = "删除文件")
    @Log(value = "删除文件")
    @DeleteMapping
    public Result delete(@RequestBody Long[] ids) {
        localStorageService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改文件")
    @Log(value = "修改文件")
    @PreAuthorize(value = "@authorize.check('storage:edit')")
    @NoRepeatSubmit
    public Result update(@Validated @RequestBody LocalStorage resource) {
        localStorageService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询文件")
    @PreAuthorize(value = "@authorize.check('storage:list')")
    @GetMapping
    public Result page(LocalStorageQuery query) {
        return Result.success(localStorageService.page(query));
    }


    @ApiOperation(value = "导出文件")
    @Log(value = "导出文件")
    @PreAuthorize(value = "@authorize.check('storage:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, LocalStorageQuery query) throws IOException {
        localStorageService.download(response, localStorageService.list(query));
    }
}
