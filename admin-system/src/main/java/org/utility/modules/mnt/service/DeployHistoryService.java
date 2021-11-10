package org.utility.modules.mnt.service;

import org.utility.core.service.Service;
import org.utility.modules.mnt.model.DeployHistory;
import org.utility.modules.mnt.service.dto.DeployHistoryDTO;
import org.utility.modules.mnt.service.dto.DeployHistoryQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 部署历史管理 服务类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public interface DeployHistoryService extends Service<DeployHistoryDTO, DeployHistoryQuery, DeployHistory> {

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<DeployHistoryDTO> queryAll) throws IOException;
}
