package com.boot.admin.system.rest;

import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.system.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：服务监控管理")
@RestController
@RequestMapping(value = "/api/monitors")
@ResultWrapper
public class MonitorController {

    private final MonitorService serverService;

    public MonitorController(MonitorService serverService) {
        this.serverService = serverService;
    }

    @ApiOperation(value = "查询服务监控")
    @PreAuthorize(value = "@authorize.check('monitors:list')")
    @GetMapping
    public Map<String, Object> query() {
        return serverService.getServerInfo();
    }
}
