package com.boot.admin.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.mnt.model.Deploy;
import com.boot.admin.mnt.model.DeployHistory;
import com.boot.admin.mnt.service.DeployService;
import com.boot.admin.mnt.service.dto.DeployDTO;
import com.boot.admin.mnt.service.dto.DeployQuery;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Api(tags = "运维：部署管理")
@RestController
@RequestMapping(value = "/api/deploys")
@ResultWrapper
public class DeployController {

    private final String fileSavePath = FileUtils.getTmpDirPath() + "/";

    private final DeployService deployService;

    public DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @ApiOperation(value = "新增部署")
    @Log(value = "新增部署")
    @PreAuthorize(value = "@authorize.check('deploys:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody DeployDTO resource) {
        deployService.saveDeploy(resource);
    }

    @ApiOperation(value = "删除部署")
    @Log(value = "删除部署")
    @PreAuthorize(value = "@authorize.check('deploys:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        deployService.removeDeployByIds(ids);
    }

    @ApiOperation(value = "修改部署")
    @Log(value = "修改部署")
    @PreAuthorize(value = "@authorize.check('deploys:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody DeployDTO resource) {
        deployService.updateDeployById(resource);
    }

    @ApiOperation(value = "查询部署")
    @PreAuthorize(value = "@authorize.check('deploys:list')")
    @GetMapping
    public Page<DeployDTO> list(DeployQuery query, Page<Deploy> page) {
        return deployService.listDeploys(query, page);
    }

    @ApiOperation(value = "上传文件部署")
    @Log(value = "上传文件部署")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/uploads")
    public Map<String, Object> upload(HttpServletRequest request, @RequestBody MultipartFile file) throws Exception {
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
        return map;
    }

    @ApiOperation(value = "系统还原")
    @Log(value = "系统还原")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/server_reductions")
    public String serverReduction(@Validated @RequestBody DeployHistory resource) {
        return deployService.serverReduction(resource);
    }

    @ApiOperation(value = "服务运行状态")
    @Log(value = "服务运行状态")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/server_status")
    public String serverStatus(@Validated @RequestBody DeployDTO resource) {
        return deployService.serverStatus(resource);
    }

    @ApiOperation(value = "启动服务")
    @Log(value = "启动服务")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/start_servers")
    public String startServer(@Validated @RequestBody DeployDTO resource) {
        return deployService.startServer(resource);
    }

    @ApiOperation(value = "停止服务")
    @Log(value = "停止服务")
    @PreAuthorize(value = "@authorize.check('deploy:edit')")
    @NoRepeatSubmit
    @PostMapping(value = "/stop_servers")
    public String stopServer(@Validated @RequestBody DeployDTO resource) {
        return deployService.stopServer(resource);
    }

    @ApiOperation(value = "导出部署")
    @Log(value = "导出部署")
    @PreAuthorize(value = "@authorize.check('deploys:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, DeployQuery query) {
        deployService.exportDeploy(deployService.listDeploys(query), response);
    }
}
