package org.utility.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.scheduling.annotation.Async;
import org.utility.core.service.Service;
import org.utility.model.ColumnConfig;
import org.utility.model.GenConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 代码生成字段信息存储 服务类
 *
 * @author Li Yanfeng
 */
public interface ColumnConfigService extends Service<ColumnConfig> {

    /**
     * 插入多条记录
     *
     * @param resources 实体对象列表
     */
    void saveColumnConfig(List<ColumnConfig> resources);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeColumnConfigByIds(Collection<Long> ids);

    /**
     * 同步表数据
     *
     * @param resources    /
     * @param resourceList /
     */
    @Async
    void syncColumnConfig(List<ColumnConfig> resources, List<ColumnConfig> resourceList);

    /**
     * 根据 TableName 查询
     *
     * @param tableName 表名
     * @return 列表查询结果
     */
    default List<ColumnConfig> listColumnConfigs(String tableName) {
        return listColumnConfigs(tableName, false);
    }

    /**
     * 根据 TableName 查询
     *
     * @param tableName            表名
     * @param queryFromTheDatabase 从数据库查询
     * @return 列表查询结果
     */
    List<ColumnConfig> listColumnConfigs(String tableName, boolean queryFromTheDatabase);

    /**
     * 根据 TableName 翻页查询
     *
     * @param tableName 表名
     * @param page  分页参数对象
     * @return 翻页查询结果
     */
    Page<ColumnConfig> listColumnConfigs(String tableName, Page<ColumnConfig> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    ColumnConfig getColumnConfigById(Long id);

    /**
     * 预览
     *
     * @param genConfig     配置信息
     * @param columnConfigs 字段信息
     * @return /
     */
    List<Map<String, Object>> preview(GenConfig genConfig, List<ColumnConfig> columnConfigs);

    /**
     * 代码生成
     *
     * @param genConfig     配置信息
     * @param columnConfigs 字段信息
     */
    void generator(GenConfig genConfig, List<ColumnConfig> columnConfigs);

    /**
     * 下载
     *
     * @param genConfig     配置信息
     * @param columnConfigs 字段信息
     * @param request       请求对象
     * @param response      响应对象
     */
    void download(GenConfig genConfig, List<ColumnConfig> columnConfigs, HttpServletRequest request,
                  HttpServletResponse response);
}
