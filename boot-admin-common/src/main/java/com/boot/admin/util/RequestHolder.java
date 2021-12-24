package com.boot.admin.util;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取 HttpServletRequest
 *
 * @author Li Yanfeng
 */
public class RequestHolder {

    private static final UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer
        .newBuilder()
        .hideMatcherLoadStats()
        .withCache(10000)
        .withField(UserAgent.AGENT_NAME_VERSION)
        .build();

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getBrowser(HttpServletRequest request) {
        UserAgent.ImmutableUserAgent userAgent = userAgentAnalyzer.parse(request.getHeader("User-Agent"));
        return userAgent.get(UserAgent.AGENT_NAME_VERSION).getValue();
    }
}
