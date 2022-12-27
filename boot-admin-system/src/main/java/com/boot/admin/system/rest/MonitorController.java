package com.boot.admin.system.rest;

import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.system.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Tag(name = "系统：服务监控管理")
@RestController
@RequestMapping(value = "/monitors")
@ResultWrapper
public class MonitorController {

    private final MonitorService serverService;

    public MonitorController(MonitorService serverService) {
        this.serverService = serverService;
    }

    @Operation(summary = "查询服务监控")
    @PreAuthorize(value = "@authorize.check('monitors:list')")
    @GetMapping
    public Map<String, Object> query() {
        return serverService.getServerInfo();
    }
}
