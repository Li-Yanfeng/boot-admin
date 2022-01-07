package com.boot.admin.util;

import cn.hutool.core.date.DatePattern;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * JDK 8  新日期类 格式化与字符串转换 工具类
 *
 * @author Li Yanfeng
 */
public class DateUtils {

    /**
     * 标准日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
    /**
     * 标准日期格式：yyyy-MM-dd
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN);
    /**
     * 标准时间格式：HH:mm:ss
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN);

    /**
     * LocalDateTime 转 时间戳
     *
     * @param localDateTime /
     * @return /
     */
    public static Long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 时间戳 转 LocalDateTime
     *
     * @param timeStamp /
     * @return /
     */
    public static LocalDateTime toLocalDateTime(Long timeStamp) {
        return LocalDateTime.ofEpochSecond(timeStamp, 0, OffsetDateTime.now().getOffset());
    }

    /**
     * LocalDateTime 转 Date
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param localDateTime /
     * @return /
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 转 Date
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param localDate /
     * @return /
     */
    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atTime(LocalTime.now(ZoneId.systemDefault())));
    }

    /**
     * Date 转 LocalDateTime
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param date /
     * @return /
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 格式化日期
     *
     * @param localDateTime /
     * @param patten        /
     * @return /
     */
    public static String format(LocalDateTime localDateTime, String patten) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(patten);
        return df.format(localDateTime);
    }

    /**
     * 格式化日期
     *
     * @param localDateTime /
     * @param df            /
     * @return /
     */
    public static String format(LocalDateTime localDateTime, DateTimeFormatter df) {
        return df.format(localDateTime);
    }

    /**
     * 构建 DateTime 对象
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parse(String localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 构建 DateTime 对象
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parse(String localDateTime, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 标准日期时间格式：yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime /
     * @return /
     */
    public static String formatDateTime(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER.format(localDateTime);
    }

    /**
     * 标准日期格式：yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static String formatDate(LocalDateTime localDateTime) {
        return DATE_FORMATTER.format(localDateTime);
    }

    /**
     * 构建 DateTime 对象（标准日期格式：yyyy-MM-dd）
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseDate(String localDateTime) {
        return LocalDateTime.from(DATE_FORMATTER.parse(localDateTime));
    }
}
