package org.utility.modules.mnt.service;

import org.utility.base.Service;
import org.utility.modules.mnt.model.Database;
import org.utility.modules.mnt.service.dto.DatabaseDTO;
import org.utility.modules.mnt.service.dto.DatabaseQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 数据库管理 服务类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public interface DatabaseService extends Service<DatabaseDTO, DatabaseQuery, Database> {

    /**
     * 测试连接数据库
     *
     * @param resource /
     * @return /
     */
    boolean testConnection(Database resource);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<DatabaseDTO> queryAll) throws IOException;
}
