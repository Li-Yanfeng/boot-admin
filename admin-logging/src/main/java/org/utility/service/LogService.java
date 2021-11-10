package org.utility.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;
import org.utility.core.service.Service;
import org.utility.model.Log;
import org.utility.service.dto.LogDTO;
import org.utility.service.dto.LogQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 系统日志 服务类
 *
 * @author Li Yanfeng
 * @since 2021-04-15
 */
public interface LogService extends Service<LogDTO, LogQuery, Log> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * 保存日志数据
     * @param username 用户
     * @param browser 浏览器
     * @param ip 请求IP
     * @param joinPoint /
     * @param log 日志实体
     */
    @Async
    void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log log);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<LogDTO> queryAll) throws IOException;
}
