package org.utility.util;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li Yanfeng
 */
public class PageUtils extends PageUtil {

    /**
     * 分页
     */
    public static <T> List<T> toPage(long current, long size, List<T> list) {
        int pageNum = (int) current;
        int pageSize = (int) size;

        int fromIndex = pageNum * pageSize;
        int toIndex = pageNum * pageSize + pageSize;
        if (fromIndex > list.size()) {
            return new ArrayList();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * 分页
     */
    public static <T> Page<T> toPage(List<T> records, long total) {
        Page<T> page = new Page<>();
        return page.setRecords(records).setTotal(total);
    }
}
