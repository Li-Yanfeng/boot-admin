package com.boot.admin.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 比较 工具类
 *
 * @author Li Yanfeng
 */
public class ComparatorUtils {

    /**
     * 从源数据中查找未包含的数据
     *
     * @param sourceData      源数据
     * @param comparativeData 与之比较数据
     * @param <T>             数据类型
     * @return 操作结果
     */
    public static <T> List<T> findDataNotIncludedInSourceData(List<T> sourceData, List<T> comparativeData) {
        return sourceData.stream().filter(item -> !comparativeData.contains(item)).collect(Collectors.toList());
    }
}
