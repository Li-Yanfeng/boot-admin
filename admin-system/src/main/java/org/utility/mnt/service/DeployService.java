package org.utility.mnt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.utility.core.service.Service;
import org.utility.mnt.model.Deploy;
import org.utility.mnt.model.DeployHistory;
import org.utility.mnt.service.dto.DeployDTO;
import org.utility.mnt.service.dto.DeployQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 部署 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface DeployService extends Service<Deploy> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveDeploy(DeployDTO resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeDeployByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateDeployById(DeployDTO resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<DeployDTO> listDeploys(DeployQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  分页参数对象
     * @return 翻页查询结果
     */
    Page<DeployDTO> listDeploys(DeployQuery query, Page<Deploy> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    DeployDTO getDeployById(Long id);

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
     * @param resource 实体对象
     * @return 操作结果
     */
    String startServer(DeployDTO resource);

    /**
     * 停止服务
     *
     * @param resource 实体对象
     * @return 操作结果
     */
    String stopServer(DeployDTO resource);

    /**
     * 查询部署状态
     *
     * @param resource 实体对象
     * @return 操作结果
     */
    String serverStatus(DeployDTO resource);

    /**
     * 停止服务
     *
     * @param resource 实体对象
     * @return 操作结果
     */
    String serverReduction(DeployHistory resource);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportDeploy(List<DeployDTO> exportData, HttpServletResponse response) throws IOException;
}
