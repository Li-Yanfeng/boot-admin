package com.boot.admin.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.ValidGroup;
import com.boot.admin.mnt.model.App;
import com.boot.admin.mnt.service.AppService;
import com.boot.admin.mnt.service.dto.AppDTO;
import com.boot.admin.mnt.service.dto.AppQuery;
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
@Tag(name = "运维：应用管理")
@RestController
@RequestMapping(value = "/api/apps")
@ResultWrapper
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @Operation(summary = "新增应用")
    @Log(value = "新增应用")
    @PreAuthorize(value = "@authorize.check('apps:add')")
    @NoRepeatSubmit
    @PostMapping
    public void save(@Validated @RequestBody App resource) {
        appService.saveApp(resource);
    }

    @Operation(summary = "删除应用")
    @Log(value = "删除应用")
    @PreAuthorize(value = "@authorize.check('apps:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        appService.removeAppByIds(ids);
    }

    @Operation(summary = "修改应用")
    @Log(value = "修改应用")
    @PreAuthorize(value = "@authorize.check('apps:edit')")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated(value = ValidGroup.Update.class) @RequestBody App resource) {
        appService.updateAppById(resource);
    }

    @Operation(summary = "查询应用")
    @PreAuthorize(value = "@authorize.check('apps:list')")
    @GetMapping
    public Page<AppDTO> list(AppQuery query, Page<App> page) {
        return appService.listApps(query, page);
    }

    @Operation(summary = "导出应用")
    @Log(value = "导出应用")
    @PreAuthorize(value = "@authorize.check('apps:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, AppQuery query) {
        appService.exportApp(appService.listApps(query), response);
    }
}
