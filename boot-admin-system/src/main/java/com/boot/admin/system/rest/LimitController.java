package com.boot.admin.system.rest;

import com.boot.admin.annotation.Limit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.rest.AnonymousGetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Tag(name = "系统：限流测试管理")
@RestController
@RequestMapping(value = "/limits")
@ResultWrapper
public class LimitController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis为 interface_limit:test:/v1/limit
     */
    @Operation(summary = "限流测试")
    @Limit(name = "限流测试", key = "test", period = 60, count = 10)
    @AnonymousGetMapping
    public int test() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
