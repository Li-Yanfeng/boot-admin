package com.boot.admin.rest;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.model.QiniuConfig;
import com.boot.admin.model.QiniuContent;
import com.boot.admin.service.QiniuConfigService;
import com.boot.admin.service.QiniuContentService;
import com.boot.admin.service.dto.QiniuContentQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "工具：七牛云存储管理")
@RestController
@RequestMapping(value = "/api/qiniu")
@ResultWrapper
public class QiniuController {

    private final QiniuConfigService qiniuConfigService;
    private final QiniuContentService qiniuContentService;

    public QiniuController(QiniuConfigService qiniuConfigService, QiniuContentService qiniuContentService) {
        this.qiniuConfigService = qiniuConfigService;
        this.qiniuContentService = qiniuContentService;
    }

    @ApiOperation(value = "同步七牛云数据")
    @Log(value = "同步七牛云数据")
    @NoRepeatSubmit
    @PutMapping(value = "/synchronizes")
    public void synchronize() {
        qiniuConfigService.synchronize(qiniuConfigService.getQiniuConfig());
    }

    @ApiOperation(value = "修改七牛云存储配置")
    @Log(value = "修改七牛云存储配置")
    @NoRepeatSubmit
    @PutMapping(value = "/configs")
    public void update(@Validated @RequestBody QiniuConfig resource) {
        qiniuConfigService.updateQiniuConfig(resource);
    }

    @ApiOperation(value = "查询七牛云存储配置")
    @GetMapping(value = "/configs")
    public QiniuConfig config() {
        return qiniuConfigService.getQiniuConfig();
    }


    @ApiOperation(value = "上传文件")
    @NoRepeatSubmit
    @PostMapping
    public QiniuContent uploadFile(@RequestParam MultipartFile file) {
        return qiniuContentService.uploadContent(file, qiniuConfigService.getQiniuConfig());
    }

    @ApiOperation(value = "删除文件")
    @Log(value = "删除文件")
    @DeleteMapping(value = "/{id}")
    public void deleteFile(@PathVariable Long id) {
        qiniuContentService.removeQiniuContentByIds(CollUtil.newHashSet(id), qiniuConfigService.getQiniuConfig());
    }

    @ApiOperation(value = "删除图片")
    @Log(value = "删除图片")
    @DeleteMapping
    public void deletePicture(@RequestBody Set<Long> ids) {
        qiniuContentService.removeQiniuContentByIds(ids, qiniuConfigService.getQiniuConfig());
    }

    @ApiOperation(value = "查询文件")
    @GetMapping
    public Page<QiniuContent> list(QiniuContentQuery query, Page<QiniuContent> page) {
        return qiniuContentService.listQiniuContents(query, page);
    }

    @ApiOperation(value = "下载文件")
    @Log(value = "下载文件")
    @GetMapping(value = "/downloads/{id}")
    public Map<String, Object> download(@PathVariable Long id) {
        return new HashMap<String, Object>(1) {{
            put("url", qiniuContentService.downloadContent(qiniuContentService.getQiniuContentById(id),
                qiniuConfigService.getQiniuConfig()));
        }};
    }

    @ApiOperation(value = "导出数据")
    @Log(value = "导出数据")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, QiniuContentQuery query) throws IOException {
        qiniuContentService.exportQiniuContent(qiniuContentService.listQiniuContents(query), response);
    }
}
