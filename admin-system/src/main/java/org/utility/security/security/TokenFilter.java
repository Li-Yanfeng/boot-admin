package org.utility.security.security;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.utility.security.config.bean.SecurityProperties;
import org.utility.security.service.OnlineUserService;
import org.utility.security.service.UserCacheClean;
import org.utility.security.service.dto.OnlineUserDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * 令牌过滤器
 *
 * @author Li Yanfeng
 */
public class TokenFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    private final TokenProvider tokenProvider;
    private final OnlineUserService onlineUserService;
    private final UserCacheClean userCacheClean;

    private final SecurityProperties securityProperties;

    /**
     * @param tokenProvider      Token
     * @param securityProperties JWT
     * @param onlineUserService  用户在线
     * @param userCacheClean     用户缓存清理工具
     */
    public TokenFilter(TokenProvider tokenProvider, OnlineUserService onlineUserService,
                       UserCacheClean userCacheClean, SecurityProperties securityProperties) {
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.userCacheClean = userCacheClean;
        this.securityProperties = securityProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDTO onlineUserDto = null;
            boolean cleanUserCache = false;
            try {
                onlineUserDto = onlineUserService.getOne(securityProperties.getOnlineKey() + token);
            } catch (ExpiredJwtException e) {
                logger.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    userCacheClean.cleanUserCache(String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY)));
                }
            }
            if (onlineUserDto != null && StringUtils.hasText(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(securityProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(securityProperties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(securityProperties.getTokenStartWith(), "");
        } else {
            logger.debug("非法Token：{}", bearerToken);
        }
        return "";
    }
}
