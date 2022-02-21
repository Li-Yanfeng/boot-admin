package com.boot.admin.aspect;

import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.util.IpUtils;
import com.boot.admin.util.RequestHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 重复提交处理
 *
 * @author Li Yanfeng
 */
@Aspect
@Component
public class RepeatSubmitAspect {

    private static final Logger logger = LoggerFactory.getLogger(RepeatSubmitAspect.class);

    private final RedisTemplate<Object, Object> redisTemplate;

    public RepeatSubmitAspect(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.boot.admin.annotation.NoRepeatSubmit)")
    public void pointcut() {
    }

    /**
     * 【环绕通知】 用于拦截指定方法，判断用户当前操作是否属于重复提交
     *
     * @param joinPoint 切入点对象
     * @return Object
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        NoRepeatSubmit noRepeatSubmit = signatureMethod.getAnnotation(NoRepeatSubmit.class);
        int time = noRepeatSubmit.time();

        // 自定义 key (请求uri：请求参数), 设置 key 的过期时间
        String requestSource = IpUtils.getIp(request) + request.getRequestURI();
        String methodSign = RequestHolder.getMethodSign(signatureMethod, joinPoint.getArgs());
        String key = String.format("request-form:%s:%s", requestSource , methodSign);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new BadRequestException(UserErrorCode.USER_REPEATED_REQUEST);
        } else {
            redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), time, TimeUnit.MILLISECONDS);
            return joinPoint.proceed();
        }
    }
}
