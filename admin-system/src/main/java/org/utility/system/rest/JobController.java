package org.utility.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.validation.Update;
import org.utility.system.model.Job;
import org.utility.system.service.JobService;
import org.utility.system.service.dto.JobDTO;
import org.utility.system.service.dto.JobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "岗位管理")
@RestController
@RequestMapping(value = "/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @ApiOperation(value = "新增岗位")
    @Log(value = "新增岗位")
    @PreAuthorize(value = "@authorize.check('job:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody Job resource) {
        jobService.saveJob(resource);
    }

    @ApiOperation(value = "删除岗位")
    @Log(value = "删除岗位")
    @PreAuthorize(value = "@authorize.check('job:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        // 验证是否被关联
        jobService.verification(ids);
        jobService.removeJobByIds(ids);
    }

    @ApiOperation(value = "修改岗位")
    @Log(value = "修改岗位")
    @PreAuthorize(value = "@authorize.check('job:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody Job resource) {
        jobService.updateJobById(resource);
    }

    @ApiOperation(value = "查询岗位")
    @PreAuthorize(value = "@authorize.check('job:list')")
    @GetMapping
    public Page<JobDTO> list(JobQuery query, Page<Job> page) {
        return jobService.listJobs(query, page);
    }

    @ApiOperation(value = "导出岗位")
    @Log(value = "导出岗位")
    @PreAuthorize(value = "@authorize.check('job:list')")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, JobQuery query) throws IOException {
        jobService.exportJob(jobService.listJobs(query), response);
    }
}
