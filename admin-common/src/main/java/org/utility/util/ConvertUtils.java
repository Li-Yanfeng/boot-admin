package org.utility.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
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
    public static <T, S> List<T> convertList(Collection<S> source, Class<T> tClass) {
        return source == null ? null :
                source.stream().map(vs -> BeanUtil.copyProperties(vs, tClass)).collect(Collectors.toList());
    }

    /**
     * 源集合 -> 目标集合
     *
     * @param source 源 Bean 对象
     * @param tClass 目标 Class
     */
    public static <T, S> Set<T> convertSet(Collection<S> source, Class<T> tClass) {
        return source == null ? null :
                source.stream().map(vs -> BeanUtil.copyProperties(vs, tClass)).collect(Collectors.toSet());
    }

    /**
     * 源分页 -> 目标分页
     *
     * @param page   源 Bean 对象
     * @param tClass 目标 Class
     */
    public static <T, S> IPage<T> convertPage(IPage<S> page, Class<T> tClass) {
        if (page == null) {
            return null;
        }
        return new Page<T>()
                // 数据列表
                .setRecords(convertList(page.getRecords(), tClass))
                // 当前页
                .setCurrent(page.getCurrent())
                // 每页显示条数
                .setSize(page.getSize())
                // 总数
                .setTotal(page.getTotal())
                // 总页数
                .setPages(page.getPages());
    }

}