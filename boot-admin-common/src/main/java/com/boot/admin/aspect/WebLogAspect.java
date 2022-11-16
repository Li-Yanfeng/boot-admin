package com.boot.admin.aspect;

import cn.hutool.json.JSONUtil;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.core.model.Result;
import com.boot.admin.util.IpUtils;
import com.boot.admin.util.RequestHolder;
import com.boot.admin.util.StringUtils;
import com.boot.admin.constant.Environment;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 统一打印入参/出参日志 处理
 *
 * @author Li Yanfeng
 */
@Profile(value = {Environment.DEV})
@Aspect
@Component
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 切入点表达式的写法: 固定格式:execution(访问权限符 返回值类型 方法全类名(参数表))
     *
     * 通配符:
     *       *:
     *          1) 匹配一个或者多个字符:
     *          2) 匹配任意一个参数:
     *          3) 只能匹配一层路径
     *          4) 权限位置 *不能表示  权限位置不写也行
     *       ..:
     *          1) 匹配任意多个参数,任意类型参数
     *          2) 匹配任意多层路径
     *
     * 执行流程: 环绕前置—>普通前置—>目标方法执行—>环绕后置—>环绕最终—>普通后置—>普通最终
     */
    @Pointcut("execution(public * com.boot.admin..rest..*.*(..))")
    public void pointcut() {
    }

    /**
     * 【环绕通知】 用于拦截指定方法
     *
     * @param joinPoint 切入点对象
     * @return Object
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        // 判断类或方法上是否有注解 @ResultWrapper
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        boolean resultWrapper = signatureMethod.isAnnotationPresent(ResultWrapper.class)
            || signatureMethod.getDeclaringClass().isAnnotationPresent(ResultWrapper.class);

        // 打印响应参数
        logger.info("Response Args  : {}", JSONUtil.toJsonStr(resultWrapper ? Result.success(result) : result));
        logger.info("Time Consuming : {} ms", System.currentTimeMillis() - startTime);
        logger.info("=========================================== End ===========================================\n");
        return result;
    }

    /**
     * 【前置通知】 在 ‘接口逻辑代码’ 开始前执行
     *
     * @param joinPoint 切入点对象
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();

        // 打印请求参数
        logger.info("========================================== Start ==========================================");
        logger.info("URL            : {}", request.getRequestURL());
        logger.info("HTTP Method    : {}", request.getMethod());
        logger.info("Description    : {}", methodDescription(joinPoint));
        logger.info("Class Method   : {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        logger.info("IP             : {}", IpUtils.getIp(request));
        logger.info("Request Args   : {}", JSONUtil.toJsonStr(joinPoint.getArgs()));
    }

    /**
     * 获取方法描述
     *
     * @param joinPoint 切入点对象
     */
    private String methodDescription(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Tag tag = signatureMethod.getAnnotation(Tag.class);
        return tag != null ? tag.name() : StringUtils.EMPTY;
    }
}
