package org.utility.service;

import org.springframework.web.multipart.MultipartFile;
import org.utility.core.service.BaseService;
import org.utility.model.LocalStorage;
import org.utility.service.dto.LocalStorageQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 本地存储 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public interface LocalStorageService extends BaseService<LocalStorageQuery, LocalStorage> {

    /**
     * 上传
     *
     * @param name 文件名称
     * @param file 文件
     * @return /
     */
    LocalStorage save(String name, MultipartFile file);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param ids 主键ID列表
     */
    void removeByIds(Long[] ids);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<LocalStorage> queryAll) throws IOException;
}
