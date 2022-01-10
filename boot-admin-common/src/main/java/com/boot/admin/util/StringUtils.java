package com.boot.admin.util;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 *
 * @author Li Yanfeng
 */
public class StringUtils extends StrUtil {

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        return StrUtil.upperFirst(toCamelCase(s));
    }
}
