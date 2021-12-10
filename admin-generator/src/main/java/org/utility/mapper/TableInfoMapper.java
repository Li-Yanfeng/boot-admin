package org.utility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.utility.model.TableInfo;

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
     * @return /
     */
    List<TableInfo> selectList();

    /**
     * 翻页查询 table
     *
     * @param page      分页参数
     * @param tableName 表名
     * @return /
     */
    Page<TableInfo> selectPage(@Param("page") IPage page, @Param("tableName") String tableName);
}
