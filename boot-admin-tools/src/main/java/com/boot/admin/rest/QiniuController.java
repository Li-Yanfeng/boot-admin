package com.boot.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.model.QiniuConfig;
import com.boot.admin.model.QiniuContent;
import com.boot.admin.service.QiniuConfigService;
import com.boot.admin.service.QiniuContentService;
import com.boot.admin.service.dto.QiniuContentQuery;
import com.boot.admin.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Tag(name = "工具：七牛云存储管理")
@RestController
@RequestMapping(value = "/qiniu")
@ResultWrapper
public class QiniuController {

    private final QiniuConfigService qiniuConfigService;
    private final QiniuContentService qiniuContentService;

    public QiniuController(QiniuConfigService qiniuConfigService, QiniuContentService qiniuContentService) {
        this.qiniuConfigService = qiniuConfigService;
        this.qiniuContentService = qiniuContentService;
    }

    @Operation(summary = "同步七牛云数据")
    @Log(value = "同步七牛云数据")
    @NoRepeatSubmit
    @PutMapping(value = "/synchronizes")
    public void synchronize() {
        qiniuConfigService.synchronize(qiniuConfigService.getQiniuConfig());
    }

    @Operation(summary = "修改七牛云存储配置")
    @Log(value = "修改七牛云存储配置")
    @NoRepeatSubmit
    @PutMapping(value = "/configs")
    public void update(@Validated @RequestBody QiniuConfig resource) {
        qiniuConfigService.updateQiniuConfig(resource);
    }

    @Operation(summary = "查询七牛云存储配置")
    @GetMapping(value = "/configs")
    public QiniuConfig config() {
        return qiniuConfigService.getQiniuConfig();
    }


    @Operation(summary = "上传文件")
    @NoRepeatSubmit
    @PostMapping
    public QiniuContent uploadFile(@RequestParam MultipartFile file) {
        return qiniuContentService.uploadContent(file, qiniuConfigService.getQiniuConfig());
    }

    @Operation(summary = "上传图片")
    @NoRepeatSubmit
    @PostMapping(value = "/pictures")
    public QiniuContent uploadPicture(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为图片
        String suffix = FileUtils.getExtensionName(file.getOriginalFilename());
        if (!FileUtils.IMAGE.equals(FileUtils.getFileType(suffix))) {
            throw new BadRequestException(UserErrorCode.USER_UPLOAD_FILE_TYPE_DOES_NOT_MATCH);
        }
        return qiniuContentService.uploadContent(file, qiniuConfigService.getQiniuConfig());
    }

    @Operation(summary = "删除文件")
    @Log(value = "删除文件")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        qiniuContentService.removeQiniuContentByIds(ids, qiniuConfigService.getQiniuConfig());
    }

    @Operation(summary = "查询文件")
    @GetMapping
    public Page<QiniuContent> list(QiniuContentQuery query, Page<QiniuContent> page) {
        return qiniuContentService.listQiniuContents(query, page);
    }

    @Operation(summary = "下载文件")
    @Log(value = "下载文件")
    @GetMapping(value = "/downloads/{id}")
    public Map<String, Object> download(@PathVariable Long id) {
        return new HashMap<String, Object>(1) {{
            put("url", qiniuContentService.downloadContent(qiniuContentService.getQiniuContentById(id),
                qiniuConfigService.getQiniuConfig()));
        }};
    }

    @Operation(summary = "导出数据")
    @Log(value = "导出数据")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, QiniuContentQuery query) {
        qiniuContentService.exportQiniuContent(qiniuContentService.listQiniuContents(query), response);
    }
}
