package com.boot.admin.config;

import com.boot.admin.util.FileUtils;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * 多文件 配置
 *
 * @author Li Yanfeng
 * @link https://blog.csdn.net/llibin1024530411/article/details/79474953
 */
@Configuration
public class MultipartConfig {

    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.home") + "/.admin/file/tmp";
        FileUtils.mkdir(location);
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
