package org.utility.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.utility.security.config.bean.LoginCode;
import org.utility.security.config.bean.LoginProperties;
import org.utility.security.config.bean.SecurityProperties;

/**
 * 配置文件转换Pojo类的 统一配置
 *
 * @author Li Yanfeng
 */
@Configuration
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
