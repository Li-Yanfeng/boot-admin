package org.utility.modules.quartz.service;

import org.springframework.scheduling.annotation.Async;
import org.utility.base.BaseService;
import org.utility.modules.quartz.model.QuartzJob;
import org.utility.modules.quartz.service.dto.QuartzJobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 定时任务 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public interface QuartzJobService extends BaseService<QuartzJobQuery, QuartzJob> {

    /**
     * 更新定时任务
     *
     * @param resource 数据传输对象
     */
    void updateIsPause(QuartzJob resource);

    /**
     * 执行任务
     *
     * @param resource 数据传输对象
     */
    void executionJob(QuartzJob resource);

    /**
     * 执行子任务
     *
     * @param tasks /
     * @throws InterruptedException /
     */
    @Async
    void executionSubJob(String[] tasks) throws InterruptedException;

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<QuartzJob> queryAll) throws IOException;
}
