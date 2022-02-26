package com.boot.admin.mnt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.mnt.model.Database;
import com.boot.admin.mnt.service.dto.DatabaseDTO;
import com.boot.admin.mnt.service.dto.DatabaseQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 数据库 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface DatabaseService extends Service<Database> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveDatabase(Database resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeDatabaseByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateDatabaseById(Database resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<DatabaseDTO> listDatabases(DatabaseQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<DatabaseDTO> listDatabases(DatabaseQuery query, Page<Database> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    DatabaseDTO getDatabaseById(Long id);

    /**
     * 测试连接
     *
     * @param resource 实体对象
     * @return 操作结果
     */
    boolean testConnection(Database resource);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportDatabase(List<DatabaseDTO> exportData, HttpServletResponse response) throws IOException;
}
