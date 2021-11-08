package org.utility.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.api.Result;
import org.utility.modules.mnt.model.Server;
import org.utility.modules.mnt.service.ServerService;
import org.utility.modules.mnt.service.dto.ServerQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Api(tags = "运维：服务器管理")
@RestController
@RequestMapping(value = "/api/serverDeploy")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @ApiOperation(value = "新增服务器")
    @Log(value = "新增服务器")
    @PreAuthorize(value = "@authorize.check('serverDeploy:add')")
    @NoRepeatSubmit
    @PostMapping
    public Result save(@Validated @RequestBody Server resource) {
        serverService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除服务器")
    @Log(value = "删除服务器")
    @PreAuthorize(value = "@authorize.check('serverDeploy:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        serverService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改服务器")
    @Log(value = "修改服务器")
    @PreAuthorize(value = "@authorize.check('serverDeploy:edit')")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody Server resource) {
        serverService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询服务器")
    @PreAuthorize(value = "@authorize.check('serverDeploy:list')")
    @GetMapping
    public Result page(ServerQuery query) {
        return Result.success(serverService.page(query));
    }

    @ApiOperation(value = "测试连接服务器")
    @Log(value = "测试连接服务器")
    @PreAuthorize(value = "@authorize.check('serverDeploy:add')")
    @NoRepeatSubmit
    @PostMapping("/testConnect")
    public Result testConnect(@Validated @RequestBody Server resources) {
        return Result.success(serverService.testConnect(resources));
    }

    @ApiOperation(value = "导出服务器")
    @Log(value = "导出服务器")
    @PreAuthorize(value = "@authorize.check('serverDeploy:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, ServerQuery query) throws IOException {
        serverService.download(response, serverService.list(query));
    }
}
