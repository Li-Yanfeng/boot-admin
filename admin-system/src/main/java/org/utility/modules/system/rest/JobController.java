package org.utility.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.annotation.NoRepeatSubmit;
import org.utility.core.model.Result;
import org.utility.modules.system.model.Job;
import org.utility.modules.system.service.JobService;
import org.utility.modules.system.service.dto.JobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：岗位管理")
@RestController
@RequestMapping(value = "/api/job")
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
    public Result save(@Validated @RequestBody Job resource) {
        jobService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除岗位")
    @Log(value = "删除岗位")
    @PreAuthorize(value = "@authorize.check('job:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改岗位")
    @Log(value = "修改岗位")
    @PreAuthorize(value = "@authorize.check('job:edit')")
    @NoRepeatSubmit
    @PutMapping
    public Result update(@Validated @RequestBody Job resource) {
        jobService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询岗位")
    @PreAuthorize(value = "@authorize.check('user:list','job:list')")
    @GetMapping
    public Result page(JobQuery query) {
        return Result.success(jobService.page(query));
    }

    @ApiOperation(value = "导出岗位")
    @Log(value = "导出岗位")
    @PreAuthorize(value = "@authorize.check('job:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, JobQuery query) throws IOException {
        jobService.download(response, jobService.list(query));
    }
}
