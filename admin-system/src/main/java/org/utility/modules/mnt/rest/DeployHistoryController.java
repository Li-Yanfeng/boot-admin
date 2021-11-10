package org.utility.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.core.model.Result;
import org.utility.modules.mnt.service.DeployHistoryService;
import org.utility.modules.mnt.service.dto.DeployHistoryQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Api(tags = "运维：部署历史管理")
@RestController
@RequestMapping(value = "/api/deployHistory")
public class DeployHistoryController {

    private final DeployHistoryService deployHistoryService;

    public DeployHistoryController(DeployHistoryService deployHistoryService) {
        this.deployHistoryService = deployHistoryService;
    }

    @ApiOperation(value = "删除部署历史")
    @Log(value = "删除部署历史")
    @PreAuthorize(value = "@authorize.check('deployHistory:del')")
    @DeleteMapping
    public Result delete(@RequestBody Set<Long> ids) {
        deployHistoryService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "查询部署历史")
    @PreAuthorize(value = "@authorize.check('deployHistory:list')")
    @GetMapping
    public Result page(DeployHistoryQuery query) {
        return Result.success(deployHistoryService.page(query));
    }

    @ApiOperation(value = "导出部署历史")
    @Log(value = "导出部署历史")
    @PreAuthorize(value = "@authorize.check('deployHistory:list')")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, DeployHistoryQuery query) throws IOException {
        deployHistoryService.download(response, deployHistoryService.list(query));
    }
}
