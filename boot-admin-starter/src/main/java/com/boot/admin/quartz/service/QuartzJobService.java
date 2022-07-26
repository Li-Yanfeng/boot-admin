package com.boot.admin.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.quartz.model.QuartzJob;
import com.boot.admin.quartz.service.dto.QuartzJobQuery;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 定时任务 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface QuartzJobService extends Service<QuartzJob> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveQuartzJob(QuartzJob resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeQuartzJobByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateQuartzJobById(QuartzJob resource);

    /**
     * 修改定时任务
     *
     * @param resource 实体对象
     */
    void updateQuartzJobByIsPause(QuartzJob resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<QuartzJob> listQuartzJobs(QuartzJobQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<QuartzJob> listQuartzJobs(QuartzJobQuery query, Page<QuartzJob> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    QuartzJob getQuartzJobById(Long id);

    /**
     * 执行定时任务
     *
     * @param resource 实体对象
     */
    void executionJob(QuartzJob resource);

    /**
     * 执行子任务
     *
     * @param tasks 主键ID列表
     * @throws InterruptedException /
     */
    @Async
    void executionSubJob(String[] tasks) throws InterruptedException;

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     */
    void exportQuartzJob(List<QuartzJob> exportData, HttpServletResponse response);
}
