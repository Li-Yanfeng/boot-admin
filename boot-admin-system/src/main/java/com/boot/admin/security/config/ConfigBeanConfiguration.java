package com.boot.admin.security.config;

import com.boot.admin.security.config.bean.LoginCode;
import com.boot.admin.security.config.bean.LoginProperties;
import com.boot.admin.security.config.bean.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件转换Pojo类的 统一配置
 *
 * @author Li Yanfeng
 */
@Configuration("systemConfigBeanConfiguration")
public class ConfigBeanConfiguration {

    @ConfigurationProperties(prefix = "login.login-code")
    @Bean
    public LoginCode loginCode() {
        return new LoginCode();
    }

    @ConfigurationProperties(prefix = "login")
    @Bean
    public LoginProperties loginProperties() {
        return new LoginProperties();
    }

    @ConfigurationProperties(prefix = "jwt")
    @Bean
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}
