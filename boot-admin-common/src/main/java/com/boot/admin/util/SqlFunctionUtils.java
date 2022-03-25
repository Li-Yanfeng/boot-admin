package com.boot.admin.util;

import java.io.Serializable;
import java.util.Date;

/**
 * Sql函数 工具类
 *
 * @author Li Yanfeng
 */
public class SqlFunctionUtils {

    /**
     * 标准日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_FORMATTER = "%Y-%m-%d %H:%i:%s";
    /**
     * 标准日期格式：yyyy-MM-dd
     */
    public static final String DATE_FORMATTER = "%Y-%m-%d";
    /**
     * 标准时间格式：HH:mm:ss
     */
    public static final String TIME_FORMATTER = "%H:%i:%s";


    /**
     * 查询字段 strList 中包含 str的结果，返回结果为null或记录
     *
     * @param str     要查询的字符串
     * @param strList 字段名。参数以”,”分隔
     */
    public static String findInSet(Serializable str, String strList) {
        return String.format("FIND_IN_SET(%s, %s)", str, strList);
    }


    /**
     * DATE_FORMAT() 函数用于以不同的格式显示日期/时间数据
     *
     * @param date   合法的日期
     * @param format 规定日期/时间的输出格式
     */
    public static String dateFormat(Date date, String format) {
        return String.format("DATE_FORMAT(%s, '%s')", date, format);
    }
}
