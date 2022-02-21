package com.boot.admin.aspect;

import com.boot.admin.annotation.Limit;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.util.IpUtils;
import com.boot.admin.util.RequestHolder;
import com.boot.admin.util.StringUtils;
import com.boot.admin.util.enums.LimitType;
import com.google.common.collect.ImmutableList;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 限流处理
 *
 * @author Li Yanfeng
 */
@Aspect
@Component
public class LimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    private final RedisTemplate<Object, Object> redisTemplate;

    public LimitAspect(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Pointcut("@annotation(com.boot.admin.annotation.Limit)")
    public void pointcut() {
    }

    /**
     * 【环绕通知】 用于拦截指定方法，判断用户当前操作是否需要限流处理
     *
     * @param joinPoint 切入点对象
     * @return Object
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        LimitType limitType = limit.limitType();
        String key = limit.key();
        // 获取 限流 类型
        if (StringUtils.isEmpty(key)) {
            key = limitType == LimitType.IP ? IpUtils.getIp(request) : signatureMethod.getName();
        }

        // 自定义key
        ImmutableList<Object> keys = ImmutableList.of("interface-limit:" + key + ":" + request.getRequestURI());

        // 构建脚本
        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Number count = redisTemplate.execute(redisScript, keys, limit.count(), limit.period());
        // 如果访问次数大于设置的次数 则 访问次数受限制
        if (null != count && count.intValue() <= limit.count()) {
            logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name());
            return joinPoint.proceed();
        } else {
            throw new BadRequestException(UserErrorCode.THE_NUMBER_OF_REQUESTS_EXCEEDS_THE_LIMIT);
        }
    }

    /**
     * 限流脚本
     */
    private String buildLuaScript() {
        return "local c" +
            "\nc = redis.call('get',KEYS[1])" +
            "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
            "\nreturn c;" +
            "\nend" +
            "\nc = redis.call('incr',KEYS[1])" +
            "\nif tonumber(c) == 1 then" +
            "\nredis.call('expire',KEYS[1],ARGV[2])" +
            "\nend" +
            "\nreturn c;";
    }
}
