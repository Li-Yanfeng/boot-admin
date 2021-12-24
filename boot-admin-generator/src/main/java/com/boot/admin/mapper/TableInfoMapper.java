package com.boot.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.mapper.BaseMapper;
import com.boot.admin.model.TableInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 表的数据信息 Mapper 接口
 *
 * @author Li Yanfeng
 */
@Repository
public interface TableInfoMapper extends BaseMapper<TableInfo> {

    /**
     * 查询 table 列表
     *
     * @return 列表查询结果
     */
    List<TableInfo> selectList();

    /**
     * 翻页查询 table
     *
     * @param page      翻页查询参数
     * @param tableName 表名
     * @return 翻页查询结果
     * I
     */
    Page<TableInfo> selectPage(@Param("page") Page<TableInfo> page, @Param("tableName") String tableName);
}
