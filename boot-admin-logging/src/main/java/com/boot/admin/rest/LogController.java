package com.boot.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.service.LogService;
import com.boot.admin.service.dto.LogDTO;
import com.boot.admin.service.dto.LogQuery;
import com.boot.admin.service.dto.LogSmallDTO;
import com.boot.admin.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Li Yanfeng
 */
@Tag(name = "系统：日志管理")
@RestController
@RequestMapping(value = "/api/logs")
@ResultWrapper
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @Operation(summary = "删除 INFO 日志")
    @Log(value = "删除 INFO 日志")
    @PreAuthorize(value = "@authorize.check('logs:del')")
    @DeleteMapping(value = "/infos")
    public void deleteAllInfoLog() {
        logService.lambdaUpdate().eq(com.boot.admin.model.Log::getLogType, LogLevel.INFO.name()).remove();
    }

    @Operation(summary = "删除 ERROR 日志")
    @Log(value = "删除 ERROR 日志")
    @PreAuthorize(value = "@authorize.check('logs:del')")
    @DeleteMapping(value = "/errors")
    public void deleteAllErrorLog() {
        logService.lambdaUpdate().eq(com.boot.admin.model.Log::getLogType, LogLevel.ERROR.name()).remove();
    }

    @Operation(summary = "查询日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping
    public Page<LogDTO> list(LogQuery query, Page<com.boot.admin.model.Log> page) {
        query.setLogType(LogLevel.INFO.name());
        return logService.listLogs(query, page);
    }

    @Operation(summary = "查询用户日志")
    @GetMapping(value = "/users")
    public Page<LogSmallDTO> listUserLogs(LogQuery query, Page<com.boot.admin.model.Log> page) {
        query.setLogType(LogLevel.INFO.name());
        query.setCreateBy(SecurityUtils.getCurrentUserId());
        return logService.listLogsByUser(query, page);
    }

    @Operation(summary = "查询错误日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/errors")
    public Page<LogDTO> listErrorLogs(LogQuery query, Page<com.boot.admin.model.Log> page) {
        query.setLogType(LogLevel.ERROR.name());
        return logService.listLogs(query, page);
    }

    @Operation(summary = "查询错误日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/errors/{id}")
    public LogDTO getErrorLogById(@PathVariable Long id) {
        return logService.getLogById(id);
    }

    @Operation(summary = "导出日志")
    @Log(value = "导出日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, LogQuery query) {
        query.setLogType(LogLevel.INFO.name());
        logService.exportLog(logService.listLogs(query), response);
    }

    @Operation(summary = "导出错误日志")
    @Log(value = "导出错误日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/exports/errors")
    public void exportErrorLog(HttpServletResponse response, LogQuery query) {
        query.setLogType(LogLevel.ERROR.name());
        logService.exportLog(logService.listLogs(query), response);
    }
}
