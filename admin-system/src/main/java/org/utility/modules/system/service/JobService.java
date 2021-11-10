package org.utility.modules.system.service;

import org.utility.core.service.Service;
import org.utility.modules.system.model.Job;
import org.utility.modules.system.service.dto.JobDTO;
import org.utility.modules.system.service.dto.JobQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 岗位 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface JobService extends Service<JobDTO, JobQuery, Job> {

    /**
     * 验证是否被用户关联
     *
     * @param ids 岗位id集合
     */
    void verification(Set<Long> ids);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<JobDTO> queryAll) throws IOException;
}
