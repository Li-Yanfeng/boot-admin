package com.boot.admin.service;

import com.boot.admin.core.service.Service;
import com.boot.admin.model.GenConfig;

import java.util.Collection;

/**
 * 代码生成配置 服务类
 *
 * @author Li Yanfeng
 */
public interface GenConfigService extends Service<GenConfig> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveGenConfig(GenConfig resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeGenConfigByIds(Collection<Long> ids);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    GenConfig getGenConfigById(Long id);

    /**
     * 根据 TableName 查询
     *
     * @param tableName 表名
     * @return 实体对象
     */
    GenConfig getGenConfigByTableName(String tableName);
}
