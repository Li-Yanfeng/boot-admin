package com.boot.admin.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.mnt.model.DeployHistory;
import com.boot.admin.mnt.service.DeployHistoryService;
import com.boot.admin.mnt.service.dto.DeployHistoryDTO;
import com.boot.admin.mnt.service.dto.DeployHistoryQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Api(tags = "运维：部署历史管理")
@RestController
@RequestMapping(value = "/api/deploy_histories")
@ResultWrapper
public class DeployHistoryController {

    private final DeployHistoryService deployHistoryService;

    public DeployHistoryController(DeployHistoryService deployHistoryService) {
        this.deployHistoryService = deployHistoryService;
    }

    @ApiOperation(value = "删除部署历史")
    @Log(value = "删除部署历史")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        deployHistoryService.removeDeployHistoryByIds(ids);
    }

    @ApiOperation(value = "查询部署历史")
    @PreAuthorize(value = "@authorize.check('deployHistories:list')")
    @GetMapping
    public Page<DeployHistoryDTO> list(DeployHistoryQuery query, Page<DeployHistory> page) {
        return deployHistoryService.listDeployHistorys(query, page);
    }

    @ApiOperation(value = "导出部署历史")
    @Log(value = "导出部署历史")
    @PreAuthorize(value = "@authorize.check('deployHistories:list')")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, DeployHistoryQuery query) {
        deployHistoryService.exportDeployHistory(deployHistoryService.listDeployHistorys(query), response);
    }
}
