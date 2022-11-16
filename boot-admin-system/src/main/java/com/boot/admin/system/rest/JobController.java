package com.boot.admin.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.system.model.Job;
import com.boot.admin.system.service.JobService;
import com.boot.admin.system.service.dto.JobDTO;
import com.boot.admin.system.service.dto.JobQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Tag(name = "系统：岗位管理")
@RestController
@RequestMapping(value = "/api/jobs")
@ResultWrapper
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "新增岗位")
    @Log(value = "新增岗位")
    @PreAuthorize(value = "@authorize.check('jobs:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Job resource) {
        jobService.saveJob(resource);
    }

    @Operation(summary = "删除岗位")
    @Log(value = "删除岗位")
    @PreAuthorize(value = "@authorize.check('jobs:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        // 验证是否被关联
        jobService.verification(ids);
        jobService.removeJobByIds(ids);
    }

    @Operation(summary = "修改岗位")
    @Log(value = "修改岗位")
    @PreAuthorize(value = "@authorize.check('jobs:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody Job resource) {
        jobService.updateJobById(resource);
    }

    @Operation(summary = "查询岗位")
    @PreAuthorize(value = "@authorize.check('jobs:list')")
    @GetMapping
    public Page<JobDTO> list(JobQuery query, Page<Job> page) {
        return jobService.listJobs(query, page);
    }

    @Operation(summary = "导出岗位")
    @Log(value = "导出岗位")
    @PreAuthorize(value = "@authorize.check('jobs:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, JobQuery query) {
        jobService.exportJob(jobService.listJobs(query), response);
    }
}
