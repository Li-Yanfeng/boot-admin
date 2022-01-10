package com.boot.admin.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import com.boot.admin.resolver.RequestArgumentResolver;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

/**
 * 配置程序适配器
 *
 * @author Li Yanfeng
 */
@Configuration
@EnableWebMvc
public class ConfigurerAdapter implements WebMvcConfigurer {

    /**
     * 文件配置
     */
    private final FileProperties fileProperties;

    public ConfigurerAdapter(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 设置允许凭据
        config.setAllowCredentials(true);
        // 添加允许的原点
        config.addAllowedOrigin("*");
        // 添加允许标题
        config.addAllowedHeader("*");
        // 添加允许的方法
        config.addAllowedMethod("*");
        // 注册CORS配置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * 添加参数解析器
     *
     * @param resolvers 解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    /**
     * 添加拦截器
     *
     * @param registry 资源处理程序注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    /**
     * 添加页面跳转
     *
     * @param registry 资源处理程序注册
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    /**
     * 添加静态资源处理程序
     *
     * @param registry 资源处理程序注册
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        FileProperties.AdminPath path = fileProperties.getPath();
        String avatarUtl = "file:" + path.getAvatar().replace("\\", "/");
        String pathUtl = "file:" + path.getPath().replace("\\", "/");
        registry.addResourceHandler("/avatar/**").addResourceLocations(avatarUtl).setCachePeriod(0);
        registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
    }

    /**
     * 配置视图解析器
     *
     * @param registry 资源处理程序注册
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

    }

    /**
     * 配置消息转换器
     *
     * @param converters 转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 处理中文乱码问题
        List<MediaType> supportMediaTypeList = CollUtil.newArrayList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);

        // 添加 jackson 配置信息
        ObjectMapper om = new ObjectMapper();
        // 反序列化时忽略json中存在但Java对象不存在的属性
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 序列化时忽略值为null的属性
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化时忽略值为默认值的属性
        om.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        // 序列化时日期格式，默认为yyyy-MM-dd'T'HH:mm:ss.SSSZ
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 序列化时自定义时间日期格式
        om.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
        // 序列化时设置时区
        om.setTimeZone(TimeZone.getDefault());
        // 统一返回数据的输出风格 (返回参数转为下划线) {link: https://adolphor.com/2019/11/16/spring-boot-under-lower-camel}
        om.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        // 解决jdk1.8 LocalDateTime 时间反序列化的问题
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        om.registerModule(javaTimeModule);
        // 处理接收RequestBody中JSON或XML对象参数
        om.registerModule(new JsonParameterTrimModule());

        // 在 convert 中添加配置信息
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(om);
        jackson2HttpMessageConverter.setSupportedMediaTypes(supportMediaTypeList);
        jackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(jackson2HttpMessageConverter);
    }
}
