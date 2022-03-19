package com.boot.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.core.validation.Update;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.model.LocalStorage;
import com.boot.admin.service.LocalStorageService;
import com.boot.admin.service.dto.LocalStorageQuery;
import com.boot.admin.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "工具：本地存储管理")
@RestController
@RequestMapping(value = "/api/local_storages")
@ResultWrapper
public class LocalStorageController {

    private final LocalStorageService localStorageService;

    public LocalStorageController(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
    }

    @ApiOperation(value = "上传文件")
    @NoRepeatSubmit
    @PostMapping
    public LocalStorage uploadFile(@RequestParam("file") MultipartFile file) {
        return localStorageService.uploadLocalStorage(file);
    }

    @ApiOperation(value = "上传图片")
    @NoRepeatSubmit
    @PostMapping(value = "/pictures")
    public LocalStorage uploadPicture(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为图片
        String suffix = FileUtils.getExtensionName(file.getOriginalFilename());
        if (!FileUtils.IMAGE.equals(FileUtils.getFileType(suffix))) {
            throw new BadRequestException(UserErrorCode.USER_UPLOAD_FILE_TYPE_DOES_NOT_MATCH);
        }
        return localStorageService.uploadLocalStorage(file);
    }

    @ApiOperation(value = "删除文件")
    @Log(value = "删除文件")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        localStorageService.removeLocalStorageByIds(ids);
    }

    @ApiOperation(value = "修改文件")
    @Log(value = "修改文件")
    @PreAuthorize(value = "@authorize.check('storages:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody LocalStorage resource) {
        localStorageService.updateLocalStorageById(resource);
    }

    @ApiOperation(value = "查询文件")
    @PreAuthorize(value = "@authorize.check('storages:list')")
    @GetMapping
    public Page<LocalStorage> list(LocalStorageQuery query, Page<LocalStorage> page) {
        return localStorageService.listLocalStorages(query, page);
    }

    @ApiOperation(value = "导出文件")
    @Log(value = "导出文件")
    @PreAuthorize(value = "@authorize.check('storages:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, LocalStorageQuery query) throws IOException {
        localStorageService.exportLocalStorage(localStorageService.listLocalStorages(query), response);
    }
}
