package com.boot.admin.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.core.validation.Update;
import com.boot.admin.mnt.model.App;
import com.boot.admin.mnt.service.AppService;
import com.boot.admin.mnt.service.dto.AppDTO;
import com.boot.admin.mnt.service.dto.AppQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "运维：应用管理")
@RestController
@RequestMapping(value = "/api/apps")
@ResultWrapper
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @ApiOperation(value = "新增应用")
    @Log(value = "新增应用")
    @PreAuthorize(value = "@authorize.check('apps:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody App resource) {
        appService.saveApp(resource);
    }

    @ApiOperation(value = "删除应用")
    @Log(value = "删除应用")
    @PreAuthorize(value = "@authorize.check('apps:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        appService.removeAppByIds(ids);
    }

    @ApiOperation(value = "修改应用")
    @Log(value = "修改应用")
    @PreAuthorize(value = "@authorize.check('apps:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = Update.class) @RequestBody App resource) {
        appService.updateAppById(resource);
    }

    @ApiOperation(value = "查询应用")
    @PreAuthorize(value = "@authorize.check('apps:list')")
    @GetMapping
    public Page<AppDTO> list(AppQuery query, Page<App> page) {
        return appService.listApps(query, page);
    }

    @ApiOperation(value = "导出应用")
    @Log(value = "导出应用")
    @PreAuthorize(value = "@authorize.check('apps:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, AppQuery query) throws IOException {
        appService.exportApp(appService.listApps(query), response);
    }
}
