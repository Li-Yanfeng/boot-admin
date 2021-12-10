package org.utility.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utility.model.Log;
import org.utility.service.LogService;
import org.utility.service.dto.LogDTO;
import org.utility.service.dto.LogQuery;
import org.utility.service.dto.LogSmallDTO;
import org.utility.util.SecurityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Li Yanfeng
 */
@Api(tags = "日志管理")
@RestController
@RequestMapping(value = "/v1/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @ApiOperation(value = "删除 INFO 日志")
    @org.utility.annotation.Log(value = "删除 INFO 日志")
    @PreAuthorize(value = "@authorize.check('logs:del')")
    @DeleteMapping(value = "/del/info")
    public void deleteAllInfoLog() {
        logService.lambdaUpdate().eq(Log::getLogType, LogLevel.INFO.name()).remove();
    }

    @ApiOperation(value = "删除 ERROR 日志")
    @org.utility.annotation.Log(value = "删除 ERROR 日志")
    @PreAuthorize(value = "@authorize.check('logs:del')")
    @DeleteMapping(value = "/del/error")
    public void deleteAllErrorLog() {
        logService.lambdaUpdate().eq(Log::getLogType, LogLevel.ERROR.name()).remove();
    }

    @ApiOperation(value = "查询日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping
    public Page<LogDTO> list(LogQuery query, Page<Log> page) {
        query.setLogType(LogLevel.INFO.name());
        return logService.listLogs(query, page);
    }

    @ApiOperation(value = "查询用户日志")
    @GetMapping(value = "/user")
    public Page<LogSmallDTO> listUserLogs(LogQuery query, Page<Log> page) {
        query.setLogType(LogLevel.INFO.name());
        query.setBlurry(SecurityUtils.getCurrentUsername());
        return logService.listLogsByUser(query, page);
    }

    @ApiOperation(value = "查询错误日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/error")
    public Page<LogDTO> listErrorLogs(LogQuery query, Page<Log> page) {
        query.setLogType(LogLevel.ERROR.name());
        return logService.listLogs(query, page);
    }

    @ApiOperation(value = "查询错误日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/error/{id}")
    public LogDTO getErrorLogById(@PathVariable Long id) {
        return logService.getLogById(id);
    }

    @ApiOperation(value = "导出日志")
    @org.utility.annotation.Log(value = "导出日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, LogQuery query) throws IOException {
        query.setLogType(LogLevel.INFO.name());
        logService.exportLog(logService.listLogs(query), response);
    }

    @ApiOperation(value = "导出错误日志")
    @org.utility.annotation.Log(value = "导出错误日志")
    @PreAuthorize(value = "@authorize.check('logs:list')")
    @GetMapping(value = "/export/error")
    public void exportErrorLog(HttpServletResponse response, LogQuery query) throws IOException {
        query.setLogType(LogLevel.ERROR.name());
        logService.exportLog(logService.listLogs(query), response);
    }
}
