package com.boot.admin.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.system.model.Job;
import com.boot.admin.system.service.dto.JobDTO;
import com.boot.admin.system.service.dto.JobQuery;
import com.boot.admin.system.service.dto.JobSmallDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 岗位 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface JobService extends Service<Job> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveJob(Job resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeJobByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateJobById(Job resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<JobDTO> listJobs(JobQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<JobDTO> listJobs(JobQuery query, Page<Job> page);

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return 列表查询结果
     */
    List<JobSmallDTO> listJobsByUserId(Long userId);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    JobDTO getJobById(Long id);

    /**
     * 验证是否被关联
     *
     * @param ids 岗位ID列表
     */
    void verification(Collection<Long> ids);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportJob(List<JobDTO> exportData, HttpServletResponse response) throws IOException;
}
