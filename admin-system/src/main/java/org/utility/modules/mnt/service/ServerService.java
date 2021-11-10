package org.utility.modules.mnt.service;

import org.utility.core.service.Service;
import org.utility.modules.mnt.model.Server;
import org.utility.modules.mnt.service.dto.ServerDTO;
import org.utility.modules.mnt.service.dto.ServerQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 服务器管理 服务类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public interface ServerService extends Service<ServerDTO, ServerQuery, Server> {

    /**
     * 根据 ip 获取
     *
     * @param ip ip地址
     * @return /
     */
    ServerDTO getByIp(String ip);

    /**
     * 测试连接
     *
     * @param resource /
     * @return /
     */
    Boolean testConnect(Server resource);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<ServerDTO> queryAll) throws IOException;
}
