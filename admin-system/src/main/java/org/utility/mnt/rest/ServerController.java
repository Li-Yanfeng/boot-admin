package org.utility.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.validation.Update;
import org.utility.mnt.model.Server;
import org.utility.mnt.service.ServerService;
import org.utility.mnt.service.dto.ServerDTO;
import org.utility.mnt.service.dto.ServerQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "运维：服务器管理")
@RestController
@RequestMapping(value = "/v1/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @ApiOperation(value = "新增服务器")
    @Log(value = "新增服务器")
    @PreAuthorize(value = "@authorize.check('servers:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Server resource) {
        serverService.saveServer(resource);
    }

    @ApiOperation(value = "删除服务器")
    @Log(value = "删除服务器")
    @PreAuthorize(value = "@authorize.check('servers:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        serverService.removeServerByIds(ids);
    }

    @ApiOperation(value = "修改服务器")
    @Log(value = "修改服务器")
    @PreAuthorize(value = "@authorize.check('servers:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody Server resource) {
        serverService.updateServerById(resource);
    }

    @ApiOperation(value = "查询服务器")
    @PreAuthorize(value = "@authorize.check('servers:list')")
    @GetMapping
    public Page<ServerDTO> list(ServerQuery query, Page<Server> page) {
        return serverService.listServers(query, page);
    }

    @ApiOperation(value = "测试连接服务器")
    @Log(value = "测试连接服务器")
    @PreAuthorize(value = "@authorize.check('serverDeploy:add')")
    @NoRepeatSubmit
    @PostMapping(value = "/testConnect")
    public Boolean testConnect(@Validated @RequestBody Server resources) {
        return serverService.testConnect(resources);
    }

    @ApiOperation(value = "导出服务器")
    @Log(value = "导出服务器")
    @PreAuthorize(value = "@authorize.check('servers:list')")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, ServerQuery query) throws IOException {
        serverService.exportServer(serverService.listServers(query), response);
    }
}
