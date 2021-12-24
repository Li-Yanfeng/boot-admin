package com.boot.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.model.Log;
import com.boot.admin.service.dto.LogDTO;
import com.boot.admin.service.dto.LogQuery;
import com.boot.admin.service.dto.LogSmallDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 日志 服务类
 *
 * @author Li Yanfeng
 */
public interface LogService extends Service<Log> {

    /**
     * 插入一条记录
     *
     * @param username  用户
     * @param browser   浏览器
     * @param ip        请求IP
     * @param joinPoint /
     * @param resource  日志实体
     */
    @Async
    void saveLog(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeLogByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateLogById(Log resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<LogDTO> listLogs(LogQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<LogDTO> listLogs(LogQuery query, Page<Log> page);

    /**
     * 根据 User 翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<LogSmallDTO> listLogsByUser(LogQuery query, Page<Log> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    LogDTO getLogById(Long id);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportLog(List<LogDTO> exportData, HttpServletResponse response) throws IOException;
}
