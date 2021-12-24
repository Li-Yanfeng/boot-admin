package com.boot.admin.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.quartz.model.QuartzLog;
import com.boot.admin.quartz.service.dto.QuartzJobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 定时任务日志 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface QuartzLogService extends Service<QuartzLog> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveQuartzLog(QuartzLog resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeQuartzLogByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateQuartzLogById(QuartzLog resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<QuartzLog> listQuartzLogs(QuartzJobQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<QuartzLog> listQuartzLogs(QuartzJobQuery query, Page<QuartzLog> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    QuartzLog getQuartzLogById(Long id);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportQuartzLog(List<QuartzLog> exportData, HttpServletResponse response) throws IOException;
}
