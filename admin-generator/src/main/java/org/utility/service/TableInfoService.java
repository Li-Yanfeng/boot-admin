package org.utility.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.utility.core.service.Service;
import org.utility.model.TableInfo;

import java.util.List;

/**
 * 表的数据信息 服务类
 *
 * @author Li Yanfeng
 */
public interface TableInfoService extends Service<TableInfo> {

    /**
     * 查询
     *
     * @return 列表查询结果
     */
    List<TableInfo> listTableInfos();

    /**
     * 根据 query 条件翻页查询
     *
     * @param tableName 表名
     * @param page      分页参数对象
     * @return 翻页查询结果
     */
    Page<TableInfo> listTableInfos(String tableName, Page<TableInfo> page);
}
