package com.boot.admin.config;

import com.boot.admin.config.bean.SpringDocProperties;
import com.boot.admin.constant.Environment;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * SpringDoc 配置类
 *
 * @author Li Yanfeng
 */
@Profile(value = {Environment.DEV})
@Configuration
public class SpringDocConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final SpringDocProperties springDocProperties;

    public SpringDocConfig(SpringDocProperties springDocProperties) {
        this.springDocProperties = springDocProperties;
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            // 文档信息
            .info(new Info()
                .title(springDocProperties.getTitle())
                .description(springDocProperties.getDescription())
                .version("version：" + springDocProperties.getVersion()))
            .externalDocs(new ExternalDocumentation()
                .description(springDocProperties.getDescription())
                .url(springDocProperties.getServiceUrl())
            )
            // 基于JWT的认证功能
            .addSecurityItem(new SecurityRequirement()
                .addList(tokenHeader))
            .components(new Components()
                .addSecuritySchemes(
                    tokenHeader,
                    new SecurityScheme()
                        .name(tokenHeader)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER))
            );
    }

    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
            .group("default")
            .pathsToMatch("/**")
            .build();
    }
}
