package com.boot.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.model.ColumnConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 代码生成字段信息存储 Mapper 接口
 *
 * @author Li Yanfeng
 */
@Repository
public interface ColumnConfigMapper extends BaseMapper<ColumnConfig> {

    /**
     * 根据表名查询 表字段数据
     *
     * @param tableName 表名
     * @return 列表查询结构
     */
    List<ColumnConfig> selectListFromInformationSchema(@Param("tableName") String tableName);

    /**
     * 根据 TableName 翻页查询
     *
     * @param page      翻页查询参数
     * @param tableName 表名
     * @return 翻页查询结果
     */
    Page<ColumnConfig> selectPageFromInformationSchema(@Param("page") Page<ColumnConfig> page,
                                                       @Param("tableName") String tableName);
}
