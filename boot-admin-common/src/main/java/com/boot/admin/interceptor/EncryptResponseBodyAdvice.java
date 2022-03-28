package com.boot.admin.interceptor;

import cn.hutool.json.JSONUtil;
import com.boot.admin.annotation.Encrypt;
import com.boot.admin.config.bean.RsaProperties;
import com.boot.admin.constant.PackagePattern;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.SystemErrorCode;
import com.boot.admin.util.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 拦截 Controller 方法的返回值，统一对返回值/响应体进行加密处理
 *
 * @author Li Yanfeng
 * @link https://juejin.cn/post/6844903981915832327
 */
@Order(2)
@RestControllerAdvice(basePackages = PackagePattern.BASE_PATH)
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(EncryptResponseBodyAdvice.class);

    /**
     * 自定义header：加密
     */
    public static final String HEADER_ENCRYPT = "encrypt";

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
        // 判断方法或参数上是否有注加 @Encrypt
        return returnType.hasMethodAnnotation(Encrypt.class) || returnType.hasParameterAnnotation(Encrypt.class);
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
        // 将方法或参数上带有注解 @Encrypt的，进行加密处理
        return encryptResponseBody(body, response);
    }

    /**
     * 加密响应体
     */
    private Object encryptResponseBody(Object body, ServerHttpResponse response) {
        // 添加 Encrypt Header，方便前端统一处理
        response.getHeaders().add(HEADER_ENCRYPT, "true");
        try {
            logger.info("========================================= 加密开始 =========================================");
            String responseBody = JSONUtil.toJsonStr(body);
            logger.info("Response Args  : {}", responseBody);
            String encryptBody = RsaUtils.encryptByPrivateKey(RsaProperties.privateKey, responseBody);
            logger.info("Encrypt After  : {}", encryptBody);
            logger.info("========================================= 加密结束 =========================================\n");
            return encryptBody;
        } catch (Exception e) {
            logger.error("========================================= 加密错误 =========================================\n");
            throw new BadRequestException(SystemErrorCode.SYSTEM_EXECUTION_ERROR);
        }
    }
}
