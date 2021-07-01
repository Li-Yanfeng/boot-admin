package org.utility.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.api.Result;
import org.utility.modules.mnt.model.App;
import org.utility.modules.mnt.service.AppService;
import org.utility.modules.mnt.service.dto.AppQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Api(tags = "运维：应用管理")
@RestController
@RequestMapping(value = "/api/app")
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @ApiOperation(value = "新增应用")
    @Log(value = "新增应用")
    @PreAuthorize(value = "@authorize.check('app:add')")
    @PostMapping
    public Result save(@Validated @RequestBody App resource) {
        appService.save(resource);
        return Result.success();
    }

    @ApiOperation(value = "删除应用")
    @Log(value = "删除应用")
    @PreAuthorize(value = "@authorize.check('app:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        appService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "修改应用")
    @Log(value = "修改应用")
    @PreAuthorize(value = "@authorize.check('app:edit')")
    @PutMapping
    public Result update(@Validated @RequestBody App resource) {
        appService.updateById(resource);
        return Result.success();
    }

    @ApiOperation(value = "查询应用")
    @PreAuthorize(value = "@authorize.check('app:list')")
    @GetMapping
    public Result page(AppQuery query) {
        return Result.success(appService.page(query));
    }

    @ApiOperation(value = "导出应用")
    @Log(value = "导出应用")
    @PreAuthorize(value = "@authorize.check('app:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, AppQuery query) throws IOException {
        appService.download(response, appService.list(query));
    }
}
