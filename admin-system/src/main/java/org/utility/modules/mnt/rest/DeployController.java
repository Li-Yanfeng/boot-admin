package org.utility.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.model.Result;
import org.utility.modules.mnt.model.DeployHistory;
import org.utility.modules.mnt.service.DeployService;
import org.utility.modules.mnt.service.dto.DeployDTO;
import org.utility.modules.mnt.service.dto.DeployQuery;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Api(tags = "运维：部署管理")
@RestController
@RequestMapping(value = "/api/deploy")
public class DeployController {

    private final String fileSavePath = FileUtils.getTmpDirPath() + "/";

    private final DeployService deployService;

    public DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @ApiOperation(value = "新增部署")
    @Log(value = "新增部署")
    @PreAuthorize(value = "@authorize.check('deploy:add')")
    @NoRepeatSubmit
    @PostMapping
    public Result save(@Validated @RequestBody DeployDTO resource) {
        deployService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除部署")
    @Log(value = "删除部署")
    @PreAuthorize(value = "@authorize.check('deploy:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        deployService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改部署")
    @Log(value = "修改部署")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody DeployDTO resource) {
        deployService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询部署")
    @PreAuthorize(value = "@authorize.check('deploy:list')")
    @GetMapping
    public Result page(DeployQuery query) {
        return Result.success(deployService.page(query));
    }

    @ApiOperation(value = "导出部署")
    @Log(value = "导出部署")
    @PreAuthorize(value = "@authorize.check('deploy:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, DeployQuery query) throws IOException {
        deployService.download(response, deployService.list(query));
    }

    @ApiOperation(value = "上传文件部署")
    @Log(value = "上传文件部署")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/upload")
    public Result upload(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        Long id = Long.valueOf(request.getParameter("id"));
        String fileName = "";
        if (file != null) {
            fileName = file.getOriginalFilename();
            File deployFile = new File(fileSavePath + fileName);
            FileUtils.del(deployFile);
            file.transferTo(deployFile);
            //文件下一步要根据文件名字来
            deployService.deploy(fileSavePath + fileName, id);
        } else {
            System.out.println("没有找到相对应的文件");
        }
        System.out.println("文件上传的原名称为:" + Objects.requireNonNull(file).getOriginalFilename());
        Map<String, Object> map = new HashMap<>(2);
        map.put("errno", 0);
        map.put("id", fileName);
        return Result.success(map);
    }

    @ApiOperation(value = "系统还原")
    @Log(value = "系统还原")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/serverReduction")
    public Result serverReduction(@Validated @RequestBody DeployHistory resource) {
        String result = deployService.serverReduction(resource);
        return Result.success(result);
    }

    @ApiOperation(value = "服务运行状态")
    @Log(value = "服务运行状态")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/serverStatus")
    public Result serverStatus(@Validated @RequestBody DeployDTO resource) {
        String result = deployService.serverStatus(resource);
        return Result.success(result);
    }

    @ApiOperation(value = "启动服务")
    @Log(value = "启动服务")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/startServer")
    public Result startServer(@Validated @RequestBody DeployDTO resource) {
        String result = deployService.startServer(resource);
        return Result.success(result);
    }

    @ApiOperation(value = "停止服务")
    @Log(value = "停止服务")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/stopServer")
    public Result stopServer(@Validated @RequestBody DeployDTO resource) {
        String result = deployService.stopServer(resource);
        return Result.success(result);
    }
}
