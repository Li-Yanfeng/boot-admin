package org.utility.modules.security.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utility.core.model.Result;
import org.utility.core.service.dto.BaseQuery;
import org.utility.modules.security.service.OnlineUserService;
import org.utility.util.EncryptUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 */
@Api(tags = "系统：在线用户管理")
@RestController
@RequestMapping(value = "/auth/online")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    public OnlineController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @ApiOperation(value = "查询在线用户")
    @PreAuthorize(value = "@authorize.check()")
    @GetMapping
    public Result query(String filter, BaseQuery query) {
        return Result.success(onlineUserService.page(filter, query));
    }

    @ApiOperation(value = "导出数据")
    @PreAuthorize(value = "@authorize.check()")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(response, onlineUserService.list(filter));
    }

    @ApiOperation(value = "踢出用户")
    @PreAuthorize(value = "@authorize.check()")
    @DeleteMapping
    public Result delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            // 解密Key
            key = EncryptUtils.desDecrypt(key);
            onlineUserService.kickOut(key);
        }
        return Result.success();
    }
}

