package org.utility.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.api.Result;
import org.utility.model.QiniuConfig;
import org.utility.model.QiniuContent;
import org.utility.service.QiniuService;
import org.utility.service.dto.QiniuConfigQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Api(tags = "工具：七牛云存储管理")
@RestController
@RequestMapping(value = "/api/qiNiuContent")
public class QiniuController {

    private final QiniuService qiniuService;

    public QiniuController(QiniuService qiniuService) {
        this.qiniuService = qiniuService;
    }

    @ApiOperation(value = "获取七牛云配置")
    @GetMapping(value = "/config")
    public Result config() {
        return Result.success(qiniuService.getConfig());
    }

    @ApiOperation(value = "配置七牛云存储")
    @Log(value = "配置七牛云存储")
    @NoRepeatSubmit
    @PutMapping(value = "/config")
    public Result update(@Validated @RequestBody QiniuConfig qiniuConfig) {
        qiniuService.config(qiniuConfig);
        qiniuService.updateByType(qiniuConfig.getType());
        return Result.success();
    }

    @ApiOperation(value = "上传文件")
    @Log(value = "上传文件")
    @NoRepeatSubmit
    @PostMapping
    public Result upload(@RequestParam MultipartFile file) {
        QiniuContent qiniuContent = qiniuService.upload(file, qiniuService.getConfig());
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", qiniuContent.getContentId());
        map.put("errno", 0);
        map.put("data", new String[]{qiniuContent.getUrl()});
        return Result.success(map);
    }

    @ApiOperation(value = "删除文件")
    @Log(value = "删除文件")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        qiniuService.removeById(qiniuService.getByContentId(id), qiniuService.getConfig());
        return Result.success();
    }

    @ApiOperation(value = "同步七牛云数据")
    @Log(value = "同步七牛云数据")
    @NoRepeatSubmit
    @PostMapping(value = "/synchronize")
    public Result synchronize() {
        qiniuService.synchronize(qiniuService.getConfig());
        return Result.success();
    }

    @ApiOperation(value = "查询文件")
    @GetMapping
    public Result page(QiniuConfigQuery query) {
        return Result.success(qiniuService.page(query));
    }

    @ApiOperation(value = "导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, QiniuConfigQuery query) throws IOException {
        qiniuService.download(response, qiniuService.listContent(query));
    }

    @ApiOperation(value = "下载文件")
    @Log(value = "下载文件")
    @GetMapping(value = "/download/{id}")
    public Result download(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("url", qiniuService.download(qiniuService.getByContentId(id), qiniuService.getConfig()));
        return Result.success(map);
    }

    @ApiOperation(value = "删除多张图片")
    @Log(value = "删除多张图片")
    @DeleteMapping
    public Result deleteAll(@RequestBody Long[] ids) {
        qiniuService.removeByIds(ids, qiniuService.getConfig());
        return Result.success();
    }
}
