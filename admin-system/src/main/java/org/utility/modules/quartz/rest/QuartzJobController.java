package org.utility.modules.quartz.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.api.Result;
import org.utility.modules.quartz.model.QuartzJob;
import org.utility.modules.quartz.service.QuartzJobService;
import org.utility.modules.quartz.service.QuartzLogService;
import org.utility.modules.quartz.service.dto.QuartzJobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Api(tags = "系统:定时任务管理")
@RestController
@RequestMapping(value = "/api/jobs")
public class QuartzJobController {

    private final QuartzJobService quartzJobService;
    private final QuartzLogService quartzLogService;

    public QuartzJobController(QuartzJobService quartzJobService, QuartzLogService quartzLogService) {
        this.quartzJobService = quartzJobService;
        this.quartzLogService = quartzLogService;
    }

    @ApiOperation(value = "新增定时任务")
    @Log(value = "新增定时任务")
    @PreAuthorize(value = "@authorize.check('timing:add')")
    @PostMapping
    public Result save(@Validated @RequestBody QuartzJob resource) {
        quartzJobService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除定时任务")
    @Log(value = "删除定时任务")
    @PreAuthorize(value = "@authorize.check('timing:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        quartzJobService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改定时任务")
    @Log(value = "修改定时任务")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @PutMapping
    public Result update(@Validated @RequestBody QuartzJob resource) {
        quartzJobService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "更改定时任务状态")
    @Log(value = "更改定时任务状态")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id) {
        quartzJobService.updateIsPause(quartzJobService.getById(id));
        return Result.success();
    }

    @ApiOperation(value = "查询定时任务")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping
    public Result page(QuartzJobQuery query) {
        return Result.success(quartzJobService.page(query));
    }

    @ApiOperation(value = "查询任务执行日志")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/logs")
    public Result pageLog(QuartzJobQuery query) {
        return Result.success(quartzLogService.page(query));
    }

    @ApiOperation(value = "执行定时任务")
    @Log(value = "执行定时任务")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @PutMapping(value = "/exec/{id}")
    public Result execution(@PathVariable Long id) {
        quartzJobService.executionJob(quartzJobService.getById(id));
        return Result.success();
    }

    @ApiOperation(value = "导出任务数据")
    @Log(value = "导出任务数据")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, QuartzJobQuery query) throws IOException {
        quartzJobService.download(response, quartzJobService.list(query));
    }

    @ApiOperation(value = "导出任务日志数据")
    @Log(value = "导出任务日志数据")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/logs/download")
    public void downloadLog(HttpServletResponse response, QuartzJobQuery query) throws IOException {
        quartzLogService.download(response, quartzLogService.list(query));
    }
}
