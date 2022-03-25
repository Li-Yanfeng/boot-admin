package com.boot.admin.interceptor;

import cn.hutool.core.io.IoUtil;
import com.boot.admin.annotation.Decrypt;
import com.boot.admin.config.bean.RsaProperties;
import com.boot.admin.constant.PackagePattern;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.util.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 拦截 Controller 方法的请求体，统一对请求参数/请求体进行解密处理
 *
 * @author Li Yanfeng
 * @link https://juejin.cn/post/6844903981915832327
 */
@ControllerAdvice(basePackages = PackagePattern.BASE_PATH)
public class DecryptRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);

    /**
     * 首先调用以确定此拦截器是否适用
     *
     * @param methodParameter 方法参数
     * @param targetType      目标类型，不一定和方法一样
     * @param converterType   选择的转换器类型
     * @return 这个拦截器是否应该被调用
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 判断方法或参数上是否有注解 @Decrypt
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    /**
     * 在读取和转换请求正文之前第二次调用
     *
     * @param inputMessage  请求
     * @param parameter     目标方法参数
     * @param targetType    目标类型，不一定和方法一样
     * @param converterType 用于反序列化正文的转换器
     * @return 输入请求或新实例（从不 {@code null}）
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        return decryptRequestBody(inputMessage);
    }

    /**
     * 解密请求体
     */
    private HttpInputMessage decryptRequestBody(HttpInputMessage inputMessage) {
        try {
            logger.info("========================================= 解密开始 =========================================");
            String requestBody = IoUtil.read(inputMessage.getBody(), StandardCharsets.UTF_8);
            logger.info("Request Args   : {}", requestBody);
            String decryptBody = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, requestBody);
            logger.info("Decrypt After  : {}", decryptBody);
            logger.info("========================================= 解密结束 =========================================\n");

            return new HttpInputMessage() {

                @Override
                public InputStream getBody() {
                    return new ByteArrayInputStream(decryptBody.getBytes(StandardCharsets.UTF_8));
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            logger.error("========================================= 解密错误 =========================================\n");
            throw new BadRequestException(UserErrorCode.CLIENT_ERROR);
        }
    }
}
