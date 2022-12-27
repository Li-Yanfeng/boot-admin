package com.boot.admin.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.mnt.model.Server;
import com.boot.admin.mnt.service.ServerService;
import com.boot.admin.mnt.service.dto.ServerDTO;
import com.boot.admin.mnt.service.dto.ServerQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Tag(name = "运维：服务器管理")
@RestController
@RequestMapping(value = "/servers")
@ResultWrapper
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @Operation(summary = "新增服务器")
    @Log(value = "新增服务器")
    @PreAuthorize(value = "@authorize.check('servers:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Server resource) {
        serverService.saveServer(resource);
    }

    @Operation(summary = "删除服务器")
    @Log(value = "删除服务器")
    @PreAuthorize(value = "@authorize.check('servers:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        serverService.removeServerByIds(ids);
    }

    @Operation(summary = "修改服务器")
    @Log(value = "修改服务器")
    @PreAuthorize(value = "@authorize.check('servers:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody Server resource) {
        serverService.updateServerById(resource);
    }

    @Operation(summary = "查询服务器")
    @PreAuthorize(value = "@authorize.check('servers:list')")
    @GetMapping
    public Page<ServerDTO> list(ServerQuery query, Page<Server> page) {
        return serverService.listServers(query, page);
    }

    @Operation(summary = "测试连接服务器")
    @Log(value = "测试连接服务器")
    @PreAuthorize(value = "@authorize.check('servers:add')")
    @NoRepeatSubmit
    @PostMapping(value = "/test_connects")
    public Boolean testConnect(@Validated @RequestBody Server resources) {
        return serverService.testConnect(resources);
    }

    @Operation(summary = "导出服务器")
    @Log(value = "导出服务器")
    @PreAuthorize(value = "@authorize.check('servers:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, ServerQuery query) {
        serverService.exportServer(serverService.listServers(query), response);
    }
}
