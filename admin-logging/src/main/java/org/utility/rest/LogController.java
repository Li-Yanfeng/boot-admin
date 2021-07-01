package org.utility.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.api.Result;
import org.utility.service.LogService;
import org.utility.service.dto.LogQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：日志管理")
@RestController
@RequestMapping(value = "/api/log")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @ApiOperation(value = "删除日志")
    @Log(value = "删除日志")
    @PreAuthorize(value = "@authorize.check('log:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        logService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "查询日志")
    @PreAuthorize(value = "@authorize.check('log:list')")
    @GetMapping
    public Result page(LogQuery query) {
        return Result.success(logService.page(query));
    }

    @ApiOperation(value = "导出日志")
    @Log(value = "导出日志")
    @PreAuthorize(value = "@authorize.check('log:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, LogQuery query) throws IOException {
        logService.download(response, logService.list(query));
    }
}
