package com.boot.admin.util;

import cn.hutool.core.collection.CollUtil;
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
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        //记录总数
        int count = list.size();
        //页数
        int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        //开始索引
        int fromIndex;
        //结束索引
        int toIndex;
        if (pageNum > pages) {
            pageNum = pages;
        }
        if (pageNum != pages) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        return new ArrayList<>(list.subList(fromIndex, toIndex));
    }

    /**
     * 分页
     */
    public static <T> Page<T> toPage(List<T> records, long total) {
        Page<T> page = new Page<>();
        return page.setRecords(records).setTotal(total);
    }
}
