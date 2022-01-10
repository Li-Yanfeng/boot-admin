package com.boot.admin.resolver;

import com.boot.admin.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;

/**
 * 请求参数解析器
 * <p>
 *  接收前端传参（使用实体类接收）：
 *    POST请求：且使用RAW的json形式：自动转换，无需配置
 *    其他请求：包括POST请求的form表单提交形式，需要自己特殊处理
 * </p>
 *
 * @author Li Yanfeng
 * @link https://adolphor.com/2019/11/16/spring-boot-under-lower-camel
 */
public class RequestArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(RequestArgumentResolver.class);

    /**
     * 是否需要处理参数
     *
     * @param parameter 参数
     * @return 此处如果返回false , 则不执行 resolveArgument
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 全局使用返回true
        return true;
    }

    /**
     * 装载参数
     *
     * @param parameter     方法参数
     * @param mavContainer  返回视图容器
     * @param webRequest    本次请求对象
     * @param binderFactory 数据绑定工厂
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return handleParameterNames(parameter, webRequest);
    }

    /**
     * 处理参数（下划线转小驼峰）
     *
     * @param parameter  方法参数
     * @param webRequest 本次请求对象
     * @return /
     */
    private Object handleParameterNames(MethodParameter parameter, NativeWebRequest webRequest) {
        Object obj = BeanUtils.instantiateClass(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> paramNames = webRequest.getParameterNames();
        while (paramNames.hasNext()) {
            String paramName = paramNames.next();
            Object o = webRequest.getParameter(paramName);
            try {
                wrapper.setPropertyValue(StringUtils.toCamelCase(paramName), o);
            } catch (BeansException e) {
                assert o != null;
                logger.warn("获取请求参数时出错，实体类 {} 中无对应属性：{}", o.getClass().getName(), paramName);
            }
        }
        return obj;
    }
}
