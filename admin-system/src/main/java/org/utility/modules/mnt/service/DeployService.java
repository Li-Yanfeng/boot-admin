package org.utility.modules.mnt.service;

import org.utility.base.Service;
import org.utility.modules.mnt.model.Deploy;
import org.utility.modules.mnt.model.DeployHistory;
import org.utility.modules.mnt.service.dto.DeployDTO;
import org.utility.modules.mnt.service.dto.DeployQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 部署管理 服务类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public interface DeployService extends Service<DeployDTO, DeployQuery, Deploy> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param resource 数据传输对象
     */
    void save(DeployDTO resource);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 数据传输对象
     */
    void updateById(DeployDTO resource);

    /**
     * 部署文件到服务器
     *
     * @param fileSavePath 文件路径
     * @param appId        应用ID
     */
    void deploy(String fileSavePath, Long appId);

    /**
     * 启动服务
     *
     * @param resource /
     * @return /
     */
    String startServer(DeployDTO resource);

    /**
     * 停止服务
     *
     * @param resource /
     * @return /
     */
    String stopServer(DeployDTO resource);

    /**
     * 查询部署状态
     *
     * @param resource /
     * @return /
     */
    String serverStatus(DeployDTO resource);

    /**
     * 停止服务
     *
     * @param resource /
     * @return /
     */
    String serverReduction(DeployHistory resource);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<DeployDTO> queryAll) throws IOException;
}
