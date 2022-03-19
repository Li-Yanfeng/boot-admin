package com.boot.admin.config;

import cn.hutool.core.collection.CollUtil;
import com.boot.admin.config.bean.FileProperties;
import com.boot.admin.resolver.PropertyNamingStrategyParameterResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

/**
 * 配置程序适配器
 *
 * @author Li Yanfeng
 */
@Configuration
@EnableWebMvc
public class ConfigurerAdapter implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final FileProperties fileProperties;

    public ConfigurerAdapter(ObjectMapper objectMapper, FileProperties fileProperties) {
        this.objectMapper = objectMapper;
        this.fileProperties = fileProperties;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 设置允许凭据
        config.setAllowCredentials(true);
        // 添加允许的原点
        config.addAllowedOriginPattern("*");
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
        resolvers.add(new PropertyNamingStrategyParameterResolver());
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

        // 在 convert 中添加配置信息
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        // 添加 jackson 配置信息
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        jackson2HttpMessageConverter.setSupportedMediaTypes(supportMediaTypeList);
        jackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(jackson2HttpMessageConverter);
    }
}
