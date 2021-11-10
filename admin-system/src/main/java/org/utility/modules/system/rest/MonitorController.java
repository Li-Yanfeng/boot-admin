package org.utility.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utility.core.model.Result;
import org.utility.modules.system.service.MonitorService;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：服务监控管理")
@RestController
@RequestMapping(value = "/api/monitor")
public class MonitorController {

    private final MonitorService serverService;

    public MonitorController(MonitorService serverService) {
        this.serverService = serverService;
    }

    @ApiOperation(value = "查询服务监控")
    @PreAuthorize(value = "@authorize.check('monitor:list')")
    @GetMapping
    public Result query() {
        return Result.success(serverService.getServerInfo());
    }
}
