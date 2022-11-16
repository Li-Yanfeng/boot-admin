package com.boot.admin.config;

import com.boot.admin.config.bean.FileProperties;
import com.boot.admin.config.bean.QiniuProperties;
import com.boot.admin.config.bean.SpringDocProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件转换Pojo类的 统一配置
 *
 * @author Li Yanfeng
 */
@Configuration(value = "commonConfigBeanConfiguration")
public class ConfigBeanConfiguration {

    @ConfigurationProperties(prefix = "file")
    @Bean
    public FileProperties fileProperties() {
        return new FileProperties();
    }

    @ConfigurationProperties(prefix = "qiniu")
    @Bean
    public QiniuProperties qiniuProperties() {
        return new QiniuProperties();
    }

    @ConfigurationProperties(prefix = "spring-doc")
    @Bean
    public SpringDocProperties springdocProperties() {
        return new SpringDocProperties();
    }
}
