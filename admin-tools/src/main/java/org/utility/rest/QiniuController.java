package org.utility.rest;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.model.QiniuConfig;
import org.utility.model.QiniuContent;
import org.utility.service.QiniuConfigService;
import org.utility.service.QiniuContentService;
import org.utility.service.dto.QiniuContentQuery;

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
@RequestMapping(value = "/v1/qiniu_contents")
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
    @PutMapping(value = "/synchronize")
    public void synchronize() {
        qiniuConfigService.synchronize(qiniuConfigService.getQiniuConfig());
    }

    @ApiOperation(value = "修改七牛云存储配置")
    @Log(value = "修改七牛云存储配置")
    @NoRepeatSubmit
    @PutMapping(value = "/config")
    public void update(@Validated @RequestBody QiniuConfig resource) {
        qiniuConfigService.updateQiniuConfig(resource);
        qiniuConfigService.updateQiniuConfigByType(resource.getType());
    }

    @ApiOperation(value = "查询七牛云存储配置")
    @GetMapping(value = "/config")
    public QiniuConfig config() {
        return qiniuConfigService.getQiniuConfig();
    }


    @ApiOperation(value = "上传文件")
    @Log(value = "上传文件")
    @NoRepeatSubmit
    @PostMapping
    public Map<String, Object> uploadFile(@RequestParam MultipartFile file) {
        QiniuContent qiniuContent = qiniuContentService.uploadContent(file, qiniuConfigService.getQiniuConfig());
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", qiniuContent.getContentId());
        map.put("errno", 0);
        map.put("data", new String[]{qiniuContent.getUrl()});
        return map;
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
    @GetMapping(value = "/download/{id}")
    public Map<String, Object> download(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("url", qiniuContentService.downloadContent(qiniuContentService.getQiniuContentById(id),
            qiniuConfigService.getQiniuConfig()));
        return map;
    }

    @ApiOperation(value = "导出数据")
    @Log(value = "导出数据")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, QiniuContentQuery query) throws IOException {
        qiniuContentService.exportQiniuContent(qiniuContentService.listQiniuContents(query), response);
    }
}
