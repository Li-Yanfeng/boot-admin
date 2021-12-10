package org.utility.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.utility.model.Log;
import org.utility.service.LogService;
import org.utility.util.IpUtils;
import org.utility.util.RequestHolder;
import org.utility.util.SecurityUtils;
import org.utility.util.ThrowableUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志处理
 *
 * @author Li Yanfeng
 */
@Aspect
@Component
public class LogAspect {

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    private final LogService logService;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(org.utility.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        Log log = new Log(LogLevel.INFO.name(), System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.saveLog(getUsername(), RequestHolder.getBrowser(request), IpUtils.getIp(request), joinPoint, log);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = new Log(LogLevel.ERROR.name(), System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setExceptionDetail(ThrowableUtils.getStackTrace(e).getBytes());
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.saveLog(getUsername(), RequestHolder.getBrowser(request), IpUtils.getIp(request), (ProceedingJoinPoint) joinPoint, log);
    }

    private String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
