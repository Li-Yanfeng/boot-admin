package org.utility.mnt.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utility.annotation.Log;
import org.utility.mnt.model.DeployHistory;
import org.utility.mnt.service.DeployHistoryService;
import org.utility.mnt.service.dto.DeployHistoryDTO;
import org.utility.mnt.service.dto.DeployHistoryQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Api(tags = "运维：部署历史管理")
@RestController
@RequestMapping(value = "/v1/deploy_historys")
public class DeployHistoryController {

    private final DeployHistoryService deployHistoryService;

    public DeployHistoryController(DeployHistoryService deployHistoryService) {
        this.deployHistoryService = deployHistoryService;
    }

    @ApiOperation(value = "删除部署历史")
    @Log(value = "删除部署历史")
    @PreAuthorize(value = "@authorize.check('deploy_historys:del')")
    @DeleteMapping
    public void delete(@RequestBody Set<Long> ids) {
        deployHistoryService.removeDeployHistoryByIds(ids);
    }

    @ApiOperation(value = "查询部署历史")
    @PreAuthorize(value = "@authorize.check('deploy_historys:list')")
    @GetMapping
    public Page<DeployHistoryDTO> list(DeployHistoryQuery query, Page<DeployHistory> page) {
        return deployHistoryService.listDeployHistorys(query, page);
    }

    @ApiOperation(value = "导出部署历史")
    @Log(value = "导出部署历史")
    @PreAuthorize(value = "@authorize.check('deploy_historys:list')")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, DeployHistoryQuery query) throws IOException {
        deployHistoryService.exportDeployHistory(deployHistoryService.listDeployHistorys(query), response);
    }
}
