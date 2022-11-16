package com.boot.admin.security.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.security.service.OnlineUserService;
import com.boot.admin.util.EncryptUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Li Yanfeng
 */
@Tag(name = "系统：在线用户管理")
@RestController
@RequestMapping(value = "/auth/online")
@ResultWrapper
public class OnlineController {

    private final OnlineUserService onlineUserService;

    public OnlineController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @Operation(summary = "查询在线用户")
    @PreAuthorize(value = "@authorize.check()")
    @GetMapping
    public Page<?> query(String filter, Page<?> page) {
        return onlineUserService.list(filter, page);
    }

    @Operation(summary = "踢出用户")
    @PreAuthorize(value = "@authorize.check()")
    @DeleteMapping
    public void delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            // 解密Key
            key = EncryptUtils.desDecrypt(key);
            onlineUserService.kickOut(key);
        }
    }

    @Operation(summary = "导出数据")
    @PreAuthorize(value = "@authorize.check()")
    @GetMapping(value = "/exports")
    public void export(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.export(onlineUserService.list(filter), response);
    }
}

