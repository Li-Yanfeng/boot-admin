package org.utility.modules.security.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.utility.modules.security.config.bean.SecurityProperties;
import org.utility.modules.security.service.OnlineUserService;
import org.utility.modules.security.service.UserCacheClean;

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

    public TokenConfigurer(TokenProvider tokenProvider, OnlineUserService onlineUserService, UserCacheClean userCacheClean, SecurityProperties securityProperties) {
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
