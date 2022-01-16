package com.boot.admin.util;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 *
 * @author Li Yanfeng
 */
public class StringUtils extends StrUtil {

    /**
     * 将下划线方式命名的字符串转换为大驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
     * 例如：hello_world =》HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的大驼峰式命名的字符串
     */
    public static String toCapitalizeCamelCase(CharSequence name) {
        return StrUtil.upperFirst(toCamelCase(name));
    }
}
