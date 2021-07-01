package org.utility.modules.mnt.service;

import org.utility.base.Service;
import org.utility.modules.mnt.model.App;
import org.utility.modules.mnt.service.dto.AppDTO;
import org.utility.modules.mnt.service.dto.AppQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 应用管理 服务类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public interface AppService extends Service<AppDTO, AppQuery, App> {

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<AppDTO> queryAll) throws IOException;
}
