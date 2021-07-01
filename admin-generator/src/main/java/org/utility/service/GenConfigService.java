package org.utility.service;

import org.utility.model.GenConfig;

/**
 * 代码生成配置 服务类
 *
 * @author Li Yanfeng
 */
public interface GenConfigService {

    /**
     * 查询表配置
     *
     * @param tableName 表名
     * @return 表配置
     */
    GenConfig getByTableName(String tableName);

    /**
     * 更新表配置
     *
     * @param tableName 表名
     * @param genConfig 表配置
     * @return 表配置
     */
    GenConfig update(String tableName, GenConfig genConfig);
}
