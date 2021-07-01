package org.utility.util;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 分页 工具类
 *
 * @author Li Yanfeng
 */
public class PageUtils extends cn.hutool.core.util.PageUtil {

    /**
     * 转换 分页
     */
    public static <T> IPage toPage(Integer current, Integer size, List<T> list) {
        int beginIndex = current * size;
        int endIndex = current * size + size;

        List records;
        // 列表数据不足
        if (beginIndex > list.size()) {
            records = ListUtil.empty();
            // 数据不足一页
        } else if (endIndex >= list.size()) {
            records = list.subList(beginIndex, list.size());
        } else {
            records = list.subList(beginIndex, endIndex);
        }
        // 获取总页数
        int pages;
        if (list.size() % size != 0) {
            pages = (list.size() / size) + 1;
        } else {
            pages = list.size() / size;
        }

        return new Page<>()
                .setRecords(records)
                .setCurrent(current)
                .setSize(size)
                .setTotal(list.size())
                .setPages(pages);
    }
}
