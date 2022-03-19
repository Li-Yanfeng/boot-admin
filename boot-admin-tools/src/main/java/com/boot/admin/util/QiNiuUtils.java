package com.boot.admin.util;

import cn.hutool.core.date.DatePattern;
import com.qiniu.storage.Region;

import java.time.LocalDateTime;

/**
 * 七牛云存储工具类
 *
 * @author Li Yanfeng
 */
public class QiNiuUtils {

    private static final String HUAD = "华东";
    private static final String HUAB = "华北";
    private static final String HUAN = "华南";
    private static final String BEIM = "北美";

    /**
     * 得到机房的对应关系
     *
     * @param zone 机房名称
     * @return Region
     */
    public static Region getRegion(String zone) {
        if (HUAD.equals(zone)) {
            return Region.huadong();
        } else if (HUAB.equals(zone)) {
            return Region.huabei();
        } else if (HUAN.equals(zone)) {
            return Region.huanan();
        } else if (BEIM.equals(zone)) {
            return Region.beimei();
        } else {
            return Region.qvmHuadong();
        }
    }

    /**
     * 默认不指定key的情况下，以文件内容的hash值作为文件名
     *
     * @param filename 文件名称
     * @return String
     */
    public static String getKey(String filename) {
        String dateStr = DateUtils.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_MS_PATTERN);
        String suffix = FileUtils.getExtensionName(filename);
        return dateStr + "." + suffix;
    }
}
