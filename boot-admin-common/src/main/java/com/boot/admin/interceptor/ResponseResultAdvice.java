package com.boot.admin.interceptor;

import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.constant.PackagePattern;
import com.boot.admin.core.model.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 拦截 Controller 方法的返回值，统一处理返回值/响应体
 *
 * @author Li Yanfeng
 * @link https://juejin.cn/post/6986800656950493214
 */
@RestControllerAdvice(basePackages = PackagePattern.BASE_PATH)
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 是否需要处理返回结果
     * <p>
     * 通过supports方法，我们可以选择哪些类或哪些方法要对response进行处理，其余的则不处理
     * </p>
     *
     * @param methodParameter 方法参数
     * @param aClass          当前类
     * @return 此处如果返回false , 则不执行当前Advice的业务
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        /*
          判断类或方法上是否有注解 @ResultWrapper
          如果类或方法上有注解，则返回真，若没有，则返回假
         */
        return methodParameter.getMethodAnnotation(ResultWrapper.class) != null
            || methodParameter.getDeclaringClass().getAnnotation(ResultWrapper.class) != null;
    }

    /**
     * 处理返回结果，可以对响应体做一些统一的操作
     * <p>
     * 比如加密，签名
     * </p>
     *
     * @param body            响应内容
     * @param methodParameter 方法参数
     * @param mediaType       文件类型
     * @param aClass          当前类
     * @param request         请求对象
     * @param response        响应对象
     * @return 处理响应的具体业务方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<?
        extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        /*
          将类或方法上带有注解@ResponseResult的，使用统一返回结果
         */
        // 如果返回 Result,直接返回
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}
