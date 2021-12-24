package com.boot.admin.security.security;

import com.boot.admin.security.config.bean.SecurityProperties;
import com.boot.admin.security.service.OnlineUserService;
import com.boot.admin.security.service.UserCacheClean;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 令牌配置
 *
 * @author Li Yanfeng
 */
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final OnlineUserService onlineUserService;
    private final UserCacheClean userCacheClean;

    private final SecurityProperties securityProperties;

    public TokenConfigurer(TokenProvider tokenProvider, OnlineUserService onlineUserService,
                           UserCacheClean userCacheClean, SecurityProperties securityProperties) {
        this.tokenProvider = tokenProvider;
        this.onlineUserService = onlineUserService;
        this.userCacheClean = userCacheClean;
        this.securityProperties = securityProperties;
    }

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(tokenProvider, onlineUserService, userCacheClean, securityProperties);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
