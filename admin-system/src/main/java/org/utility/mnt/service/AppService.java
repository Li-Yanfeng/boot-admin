package org.utility.mnt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.utility.core.service.Service;
import org.utility.mnt.model.App;
import org.utility.mnt.service.dto.AppDTO;
import org.utility.mnt.service.dto.AppQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 应用 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface AppService extends Service<App> {

    /**
     * 插入一条记录
     *
     * @param resource 实体对象
     */
    void saveApp(App resource);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeAppByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateAppById(App resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<AppDTO> listApps(AppQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  分页参数对象
     * @return 翻页查询结果
     */
    Page<AppDTO> listApps(AppQuery query, Page<App> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    AppDTO getAppById(Long id);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportApp(List<AppDTO> exportData, HttpServletResponse response) throws IOException;
}
