package org.utility.modules.quartz.service;

import org.utility.base.BaseService;
import org.utility.modules.quartz.model.QuartzLog;
import org.utility.modules.quartz.service.dto.QuartzJobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 定时任务日志 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public interface QuartzLogService extends BaseService<QuartzJobQuery, QuartzLog> {

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<QuartzLog> queryAll) throws IOException;
}
