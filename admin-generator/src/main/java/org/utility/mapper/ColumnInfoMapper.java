package org.utility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.utility.model.ColumnInfo;
import org.utility.model.vo.TableInfo;

import java.util.List;

/**
 * 列数据信息 Mapper 接口
 *
 * @author Li Yanfeng
 */
@Repository
public interface ColumnInfoMapper extends BaseMapper<ColumnInfo> {

    /**
     * 查询 table
     *
     * @return /
     */
    List<TableInfo> selectListTables();

    /**
     * 翻页查询 table
     *
     * @param page      分页参数
     * @param tableName 表名
     * @return /
     */
    IPage<TableInfo> selectPageTables(@Param("page") IPage page, @Param("tableName") String tableName);

    /**
     * 根据表名查询 表字段数据
     *
     * @param tableName 表名
     * @return /
     */
    List<ColumnInfo> selectByTableName(@Param("tableName") String tableName);
}