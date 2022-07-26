package com.boot.admin.mnt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.mnt.model.Server;
import com.boot.admin.mnt.service.dto.ServerDTO;
import com.boot.admin.mnt.service.dto.ServerQuery;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 服务器 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface ServerService extends Service<Server> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveServer(Server resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeServerByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateServerById(Server resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<ServerDTO> listServers(ServerQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<ServerDTO> listServers(ServerQuery query, Page<Server> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    ServerDTO getServerById(Long id);

    /**
     * 根据 Ip 查询
     *
     * @param ip ip地址
     * @return 实体对象
     */
    ServerDTO getServerByIp(String ip);

    /**
     * 测试连接
     *
     * @param resource 实体对象
     * @return 操作结果
     */
    Boolean testConnect(Server resource);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     */
    void exportServer(List<ServerDTO> exportData, HttpServletResponse response);
}
