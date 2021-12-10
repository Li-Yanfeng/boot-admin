package org.utility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.utility.model.ColumnConfig;

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
     * @return /
     */
    List<ColumnConfig> selectListFromInformationSchema(@Param("tableName") String tableName);

    /**
     * 根据 TableName 翻页查询
     *
     * @param page      分页参数
     * @param tableName 表名
     * @return 翻页查询结果
     */
    Page<ColumnConfig> selectPageFromInformationSchema(@Param("page") IPage<ColumnConfig> page,
                                                       @Param("tableName") String tableName);
}
