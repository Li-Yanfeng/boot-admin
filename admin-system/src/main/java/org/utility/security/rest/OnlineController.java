package org.utility.security.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utility.security.service.OnlineUserService;
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
    public Page<?> query(String filter, Page<?> page) {
        return onlineUserService.list(filter, page);
    }

    @ApiOperation(value = "踢出用户")
    @PreAuthorize(value = "@authorize.check()")
    @DeleteMapping
    public void delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            // 解密Key
            key = EncryptUtils.desDecrypt(key);
            onlineUserService.kickOut(key);
        }
    }

    @ApiOperation(value = "导出数据")
    @PreAuthorize(value = "@authorize.check()")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.export(onlineUserService.list(filter), response);
    }
}

