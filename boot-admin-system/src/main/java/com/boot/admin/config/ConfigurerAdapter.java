package com.boot.admin.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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
    private final FileProperties properties;

    public ConfigurerAdapter(FileProperties properties) {
        this.properties = properties;
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
        FileProperties.AdminPath path = properties.getPath();
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
        ObjectMapper objectMapper = new ObjectMapper();
        // 反序列化时忽略json中存在但Java对象不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化时日期格式默认为yyyy-MM-dd'T'HH:mm:ss.SSSZ
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 序列化时自定义时间日期格式
        objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
        //序列化时设置时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //序列化时忽略值为null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //序列化时忽略值为默认值的属性
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);

        // 在 convert 中添加配置信息
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        jackson2HttpMessageConverter.setSupportedMediaTypes(supportMediaTypeList);
        jackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(jackson2HttpMessageConverter);
    }
}
