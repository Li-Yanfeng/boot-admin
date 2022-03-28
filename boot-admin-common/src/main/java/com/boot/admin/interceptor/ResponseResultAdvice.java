package com.boot.admin.interceptor;

import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.constant.PackagePattern;
import com.boot.admin.core.model.Result;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
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
@Order(1)
@RestControllerAdvice(basePackages = PackagePattern.BASE_PATH)
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 自定义header：加密
     */
    public static final String HEADER_RESULT_WRAPPER = "result-wrapper";

    /**
     * 该组件是否支持给定的控制器方法返回类型和选择的 {@code HttpMessageConverter} 类型
     *
     * @param returnType    返回类型
     * @param converterType 选择的转换器类型
     * @return {@code true} 如果 {@link #beforeBodyWrite} 应该被调用；
     * {@code false} 否则
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 判断类或方法上是否有注解 @ResultWrapper
        return returnType.getMethodAnnotation(ResultWrapper.class) != null
            || returnType.getDeclaringClass().getAnnotation(ResultWrapper.class) != null;
    }

    /**
     * 在选择 {@code HttpMessageConverter} 之后和之前调用它的 write 方法被调用
     *
     * @param body                  要写入的正文
     * @param returnType            控制器方法的返回类型
     * @param selectedContentType   通过内容协商选择的内容类型
     * @param selectedConverterType 选择写入响应的转换器类型
     * @param request               当前请求
     * @param response              当前响应
     * @return 传入的主体或修改过的（可能是新的）实例
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<?
        extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        /* 将类或方法上带有注解@ResponseResult的，使用统一返回结果 */

        // 添加 Result Wrapper Header，方便前端统一处理
        response.getHeaders().add(HEADER_RESULT_WRAPPER, "true");

        // 如果返回 Result,直接返回
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}
