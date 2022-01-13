package com.boot.admin.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.IOException;

/**
 * 全局请求参数去除空格配置
 *
 * @author Li Yanfeng
 */
public class WebMvcTrimConfiguration {

}

/**
 * 处理接收 url 或 form 表单中的参数
 */
@ControllerAdvice
class FormParameterTrim {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        /*
         构造方法中 boolean 参数含义为如果是空白字符串,是否转换为null
         即如果为true,那么 " " 会被转换为 null,否则为 ""
        */
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}

/**
 * 处理接收Request Body中JSON或XML对象参数
 */
class JsonParameterTrimModule extends SimpleModule {

    public JsonParameterTrimModule() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
                // 去除前后空格
                return StringUtils.trim(jsonParser.getValueAsString());
            }
        });
    }
}
