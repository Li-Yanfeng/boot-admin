package com.boot.admin.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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

    /**
     * 获取浏览器
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent.ImmutableUserAgent userAgent = userAgentAnalyzer.parse(request.getHeader("User-Agent"));
        return userAgent.get(UserAgent.AGENT_NAME_VERSION).getValue();
    }

    /**
     * 生成方法标记：采用数字签名算法SHA1对方法签名字符串加签
     *
     * @param method 请求方法
     * @param args   传入的参数
     * @return 请求参数
     */
    public static String getMethodSign(Method method, Object... args) {
        StringBuilder sb = new StringBuilder(method.toString());
        for (Object arg : args) {
            sb.append(JSONUtil.toJsonStr(arg));
        }
        return SecureUtil.sha1(sb.toString());
    }
}
