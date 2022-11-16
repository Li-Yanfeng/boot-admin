package com.boot.admin.quartz.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.quartz.model.QuartzJob;
import com.boot.admin.quartz.model.QuartzLog;
import com.boot.admin.quartz.service.QuartzJobService;
import com.boot.admin.quartz.service.QuartzLogService;
import com.boot.admin.quartz.service.dto.QuartzJobQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Tag(name = "系统：定时任务管理")
@RestController
@RequestMapping(value = "/api/quartz_jobs")
@ResultWrapper
public class QuartzJobController {

    private final QuartzJobService quartzJobService;
    private final QuartzLogService quartzLogService;

    public QuartzJobController(QuartzJobService quartzJobService, QuartzLogService quartzLogService) {
        this.quartzJobService = quartzJobService;
        this.quartzLogService = quartzLogService;
    }

    @Operation(summary = "新增定时任务")
    @Log(value = "新增定时任务")
    @PreAuthorize(value = "@authorize.check('timing:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody QuartzJob resource) {
        quartzJobService.saveQuartzJob(resource);
    }

    @Operation(summary = "删除定时任务")
    @Log(value = "删除定时任务")
    @PreAuthorize(value = "@authorize.check('timing:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        quartzJobService.removeQuartzJobByIds(ids);
    }

    @Operation(summary = "修改定时任务")
    @Log(value = "修改定时任务")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody QuartzJob resource) {
        quartzJobService.updateQuartzJobById(resource);
    }

    @Operation(summary = "更改定时任务状态")
    @Log(value = "更改定时任务状态")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @NoRepeatSubmit
    @PutMapping(value = "/{id}")
    public void update(@PathVariable Long id) {
        quartzJobService.updateQuartzJobByIsPause(quartzJobService.getQuartzJobById(id));
    }

    @Operation(summary = "查询定时任务")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping
    public Page<QuartzJob> listQuartzJobs(QuartzJobQuery query, Page<QuartzJob> page) {
        return quartzJobService.listQuartzJobs(query, page);
    }

    @Operation(summary = "查询任务执行日志")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/logs")
    public Page<QuartzLog> listQuartzLogs(QuartzJobQuery query, Page<QuartzLog> page) {
        return quartzLogService.listQuartzLogs(query, page);
    }

    @Operation(summary = "执行定时任务")
    @Log(value = "执行定时任务")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @NoRepeatSubmit
    @PutMapping(value = "/executions/{id}")
    public void execution(@PathVariable Long id) {
        quartzJobService.executionJob(quartzJobService.getQuartzJobById(id));
    }

    @Operation(summary = "导出任务数据")
    @Log(value = "导出任务数据")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/exports")
    public void exportQuartzJob(HttpServletResponse response, QuartzJobQuery query) {
        quartzJobService.exportQuartzJob(quartzJobService.listQuartzJobs(query), response);
    }

    @Operation(summary = "导出任务日志数据")
    @Log(value = "导出任务日志数据")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/logs/exports")
    public void exportQuartzLog(HttpServletResponse response, QuartzJobQuery query) {
        quartzLogService.exportQuartzLog(quartzLogService.listQuartzLogs(query), response);
    }
}
