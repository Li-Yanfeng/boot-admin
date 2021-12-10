package org.utility.quartz.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.validation.Update;
import org.utility.quartz.model.QuartzJob;
import org.utility.quartz.model.QuartzLog;
import org.utility.quartz.service.QuartzJobService;
import org.utility.quartz.service.QuartzLogService;
import org.utility.quartz.service.dto.QuartzJobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "系统：定时任务管理")
@RestController
@RequestMapping(value = "/v1/quartz_jobs")
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
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody QuartzJob resource) {
        quartzJobService.saveQuartzJob(resource);
    }

    @ApiOperation(value = "删除定时任务")
    @Log(value = "删除定时任务")
    @PreAuthorize(value = "@authorize.check('timing:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        quartzJobService.removeQuartzJobByIds(ids);
    }

    @ApiOperation(value = "修改定时任务")
    @Log(value = "修改定时任务")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody QuartzJob resource) {
        quartzJobService.updateQuartzJobById(resource);
    }

    @ApiOperation(value = "更改定时任务状态")
    @Log(value = "更改定时任务状态")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @NoRepeatSubmit
    @PutMapping(value = "/{id}")
    public void update(@PathVariable Long id) {
        quartzJobService.updateQuartzJobByIsPause(quartzJobService.getQuartzJobById(id));
    }

    @ApiOperation(value = "查询定时任务")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping
    public Page<QuartzJob> listQuartzJobs(QuartzJobQuery query, Page<QuartzJob> page) {
        return quartzJobService.listQuartzJobs(query, page);
    }

    @ApiOperation(value = "查询任务执行日志")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/logs")
    public Page<QuartzLog> listQuartzLogs(QuartzJobQuery query, Page<QuartzLog> page) {
        return quartzLogService.listQuartzLogs(query, page);
    }

    @ApiOperation(value = "执行定时任务")
    @Log(value = "执行定时任务")
    @PreAuthorize(value = "@authorize.check('timing:edit')")
    @NoRepeatSubmit
    @PutMapping(value = "/exec/{id}")
    public void execution(@PathVariable Long id) {
        quartzJobService.executionJob(quartzJobService.getQuartzJobById(id));
    }

    @ApiOperation(value = "导出任务数据")
    @Log(value = "导出任务数据")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/export")
    public void exportQuartzJob(HttpServletResponse response, QuartzJobQuery query) throws IOException {
        quartzJobService.exportQuartzJob(quartzJobService.listQuartzJobs(query), response);
    }

    @ApiOperation(value = "导出任务日志数据")
    @Log(value = "导出任务日志数据")
    @PreAuthorize(value = "@authorize.check('timing:list')")
    @GetMapping(value = "/logs/export")
    public void exportQuartzLog(HttpServletResponse response, QuartzJobQuery query) throws IOException {
        quartzLogService.exportQuartzLog(quartzLogService.listQuartzLogs(query), response);
    }
}
