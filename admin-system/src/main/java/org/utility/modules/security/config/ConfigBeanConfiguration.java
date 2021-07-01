package org.utility.modules.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.utility.modules.security.config.bean.LoginProperties;
import org.utility.modules.security.config.bean.SecurityProperties;

/**
 * 配置文件转换Pojo类的 统一配置
 *
 * @author Li Yanfeng
 */
@Configuration
public class ConfigBeanConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "login")
    public LoginProperties loginProperties() {
        return new LoginProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}
