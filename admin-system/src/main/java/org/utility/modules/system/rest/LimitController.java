package org.utility.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utility.annotation.Limit;
import org.utility.annotation.rest.AnonymousGetMapping;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@Api(tags = "系统：限流测试管理")
@RestController
@RequestMapping(value = "/api/limit")
public class LimitController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis为 interface_limit:test:/api/limit
     */
    @ApiOperation(value = "测试")
    @AnonymousGetMapping
    @Limit(name = "测试限流", key = "test", period = 60, count = 10)
    public int test() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
