package com.boot.admin.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 转换 工具类
 *
 * @author Li Yanfeng
 */
public class ConvertUtils {

    /**
     * 源 -> 目标
     *
     * @param source 源 Bean 对象
     * @param tClass 目标 Class
     */
    public static <T, S> T convert(final S source, Class<T> tClass) {
        return source == null ? null : BeanUtil.copyProperties(source, tClass);
    }

    /**
     * 源集合 -> 目标集合
     *
     * @param source 源 Bean 对象
     * @param tClass 目标 Class
     */
    public static <T, S> List<T> convert(List<S> source, Class<T> tClass) {
        return source == null ? null :
            source.stream().map(vs -> BeanUtil.copyProperties(vs, tClass)).collect(Collectors.toList());
    }

    /**
     * 源集合 -> 目标集合
     *
     * @param source 源 Bean 对象
     * @param tClass 目标 Class
     */
    public static <T, S> Set<T> convert(Set<S> source, Class<T> tClass) {
        return source == null ? null :
            source.stream().map(vs -> BeanUtil.copyProperties(vs, tClass)).collect(Collectors.toSet());
    }

    /**
     * 源分页 -> 目标分页
     *
     * @param page   源 Bean 对象
     * @param tClass 目标 Class
     */
    public static <T, S> Page<T> convert(Page<S> page, Class<T> tClass) {
        if (page == null) {
            return null;
        }

        Page<T> tPage = new Page<>();
        // 数据列表
        tPage.setRecords(convert(page.getRecords(), tClass));
        // 当前页
        tPage.setCurrent(page.getCurrent());
        // 每页显示条数
        tPage.setSize(page.getSize());
        // 总数
        tPage.setTotal(page.getTotal());
        // 总页数
        tPage.setPages(page.getPages());
        return tPage;
    }
}
