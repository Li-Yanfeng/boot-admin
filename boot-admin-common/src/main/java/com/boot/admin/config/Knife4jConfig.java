package com.boot.admin.config;

import cn.hutool.core.collection.CollUtil;
import com.boot.admin.config.bean.SwaggerProperties;
import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * Swagger3 配置类
 *
 * @author Li Yanfeng
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final SwaggerProperties swaggerProperties;

    public Knife4jConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
            .pathMapping("/")
            // 是否开启swagger
            .enable(swaggerProperties.getEnabled())
            // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
            .apiInfo(apiInfo())
            // 选择哪些接口作为swagger的doc发布
            .select()
            // 扫描所有有注解的api
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            // 扫描所在包下的api
            .paths(PathSelectors.regex("/error.*").negate())
            .build()
            // 添加登陆认证
            .securitySchemes(securitySchemes())
            .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(swaggerProperties.getTitle())
            .description(swaggerProperties.getDescription())
            .version("version：" + swaggerProperties.getVersion())
            .termsOfServiceUrl(swaggerProperties.getServiceUrl())
            .contact(new Contact(
                swaggerProperties.getContactName(),
                swaggerProperties.getContactUrl(),
                swaggerProperties.getContactEmail()
            ))
            .build();
    }

    private List<SecurityScheme> securitySchemes() {
        //设置请求头信息
        List<SecurityScheme> securitySchemes = CollUtil.newArrayList();
        ApiKey apiKey = new ApiKey(tokenHeader, tokenHeader, In.HEADER.toValue());
        securitySchemes.add(apiKey);
        return securitySchemes;
    }

    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> securityContexts = CollUtil.newArrayList();
        // ^(?!auth).*$ 表示所有包含auth的接口不需要使用securitySchemes即不需要带token
        // ^标识开始  ()里是一子表达式  ?!/auth表示匹配不是/auth的位置，匹配上则添加请求头，注意路径已/开头  .表示任意字符  *表示前面的字符匹配多次 $标识结束
        securityContexts.add(
            SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                .build()
        );
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> securityReferences = CollUtil.newArrayList();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        securityReferences.add(new SecurityReference(tokenHeader, authorizationScopes));
        return securityReferences;
    }
}

/**
 * 将Pageable转换展示在swagger中
 */
@Configuration
@EnableOpenApi
class Knife4jDataConfig {

    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return CollUtil.newArrayList(newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)));
            }
        };
    }

    @ApiModel
    private static class Page {

        @ApiModelProperty("页码 (0..N)")
        private Long current;

        @ApiModelProperty("每页显示的数目")
        private Long size;

        @ApiModelProperty("以下列格式排序标准：property[,asc | desc]。 默认排序顺序为升序。 支持多种排序条件：如：id,asc")
        private List<String> sort;


        public Long getCurrent() {
            return current;
        }

        public void setCurrent(Long current) {
            this.current = current;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public List<String> getSort() {
            return sort;
        }

        public void setSort(List<String> sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
        }
    }
}
