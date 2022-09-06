package com.boot.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.model.LocalStorage;
import com.boot.admin.service.dto.LocalStorageQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 本地存储 服务类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
public interface LocalStorageService extends Service<LocalStorage> {

    /**
     * 上传文件
     *
     * @param file 待上传的文件
     */
    LocalStorage uploadLocalStorage(MultipartFile file);

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
     * @param page  翻页查询对象
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
     */
    void exportLocalStorage(List<LocalStorage> exportData, HttpServletResponse response);
}
