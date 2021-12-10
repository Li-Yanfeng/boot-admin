package org.utility.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;
import org.utility.core.service.Service;
import org.utility.model.LocalStorage;
import org.utility.service.dto.LocalStorageQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 本地存储 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface LocalStorageService extends Service<LocalStorage> {

    /**
     * 上传文件
     *
     * @param filename 文件名称
     * @param file     文件
     */
    void uploadLocalStorage(String filename, MultipartFile file);

    /**
     * 根据 ID 批量删除
     *
     * @param ids 主键ID列表
     */
    void removeLocalStorageByIds(Collection<Long> ids);

    /**
     * 根据 ID 选择修改
     *
     * @param resource 实体对象
     */
    void updateLocalStorageById(LocalStorage resource);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<LocalStorage> listLocalStorages(LocalStorageQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  分页参数对象
     * @return 翻页查询结果
     */
    Page<LocalStorage> listLocalStorages(LocalStorageQuery query, Page<LocalStorage> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    LocalStorage getLocalStorageById(Long id);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportLocalStorage(List<LocalStorage> exportData, HttpServletResponse response) throws IOException;
}
