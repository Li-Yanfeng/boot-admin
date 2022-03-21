package com.boot.admin.resolver;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.boot.admin.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 属性命名参数解析器
 * <p>
 * POST请求:使用RAW的json形式：自动转换，无需配置;
 * 其他请求：包括POST请求的form表单提交形式，需要自己特殊处理;
 * </p>
 *
 * @author Li Yanfeng
 * @link https://www.jianshu.com/p/a707acd7eb3a
 */
public class PropertyNamingStrategyParameterResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(PropertyNamingStrategyParameterResolver.class);

    /**
     * 判断 HandlerMethodArgumentResolver 是否支持 MethodParameter
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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String name = ModelFactory.getNameForParameter(parameter);
        Object obj = (mavContainer.containsAttribute(name) ? mavContainer.getModel().get(name) : createAttribute(parameter));

        WebDataBinder binder = binderFactory.createBinder(webRequest, obj, name);
        if (binder.getTarget() != null) {
            if (!mavContainer.isBindingDisabled(name)) {
                bindRequestParameters(webRequest, binder, obj);
            }
            validateIfApplicable(parameter, binder);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(parameter, binder)) {
                throw new BindException(binder.getBindingResult());
            }
        }

        // Add resolved attribute and BindingResult at the end of the model
        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);

        return binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
    }

    /**
     * 创建参数实例
     *
     * @param parameter 方法参数
     * @return 参数实例
     */
    private Object createAttribute(MethodParameter parameter) {
        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    /**
     * 绑定请求参数
     *
     * @param binder     执行数据的绑定器
     * @param webRequest 本次请求对象
     */
    private void bindRequestParameters(NativeWebRequest webRequest, WebDataBinder binder, Object obj) {
        // 存放转换后的属性名称及属性值
        Map<String, Object[]> result = new HashMap<>();
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        if (MapUtil.isNotEmpty(parameterMap)) {
            Set<String> parameterNames = parameterMap.keySet();
            parameterNames.forEach(parameterName -> {
                // 将蛇形属性名转换小驼峰属性名
                String fieldName = StringUtils.toCamelCase(parameterName);
                // 传递的参数值
                String[] parameterValues = parameterMap.get(parameterName);
                try {
                    result.put(fieldName, getParameterValues(obj, fieldName, parameterValues));
                } catch (NoSuchFieldException ignored) {

                }
            });
        }
        binder.bind(new MutablePropertyValues(result));
    }

    /**
     * 验证是否适用
     *
     * @param parameter 方法参数
     * @param binder    执行数据的绑定器
     */
    private void validateIfApplicable(MethodParameter parameter, WebDataBinder binder) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
                binder.validate(validationHints);
                break;
            }
        }
    }

    /**
     * 是否在验证错误时引发致命绑定异常
     *
     * @param binder    执行数据的绑定器
     * @param parameter 方法参数
     * @return {@code true} if the next method argument is not of type {@link Errors}
     */
    private boolean isBindExceptionRequired(MethodParameter parameter, WebDataBinder binder) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }

    /**
     * 是否是数组
     */
    private boolean isArrayType(Class<?> fieldClass) {
        return fieldClass.isArray() || Collection.class.isAssignableFrom(fieldClass);
    }

    /**
     * 获取参数值
     *
     * @param obj             要设置的对象
     * @param fieldName       属性名称
     * @param parameterValues 参数值
     * @return 最终参数值
     */
    private Object[] getParameterValues(Object obj, String fieldName, String[] parameterValues) throws NoSuchFieldException {
        // 存放最终参数值
        List<Object> values = CollUtil.newArrayList();
        // 获取实体属性类型
        Class<?> fieldType = obj.getClass().getDeclaredField(fieldName).getType();
        boolean arrayType = isArrayType(fieldType);
        // 处理传递的参数值
        for (String parameterValue : parameterValues) {
            parameterValue = StringUtils.trim(parameterValue);
            if (arrayType) {
                values.addAll(CollUtil.newArrayList(StringUtils.split(parameterValue, StringUtils.COMMA)));
            } else {
                values.addAll(CollUtil.newArrayList(parameterValue));
            }
        }
        return values.toArray();
    }
}
