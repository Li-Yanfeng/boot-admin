package org.utility.service;

import org.springframework.web.multipart.MultipartFile;
import org.utility.core.service.BaseService;
import org.utility.model.QiniuConfig;
import org.utility.model.QiniuContent;
import org.utility.service.dto.QiniuConfigQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 七牛云存储 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public interface QiniuService extends BaseService<QiniuConfigQuery, QiniuConfig> {

    /**
     * 修改配置
     *
     * @param qiniuConfig 配置
     * @return /
     */
    QiniuConfig config(QiniuConfig qiniuConfig);

    /**
     * 根据 type 修改
     *
     * @param type 空间类型
     */
    void updateByType(String type);

    /**
     * 查询配置
     *
     * @return /
     */
    QiniuConfig getConfig();

    /**
     * 上传文件
     *
     * @param file        文件
     * @param qiniuConfig 配置
     * @return QiniuContent
     */
    QiniuContent upload(MultipartFile file, QiniuConfig qiniuConfig);

    /**
     * 删除文件
     *
     * @param content 文件
     * @param config  配置
     */
    void removeById(QiniuContent content, QiniuConfig config);

    /**
     * 删除文件
     *
     * @param ids    文件ID数组
     * @param config 配置
     */
    void removeByIds(Long[] ids, QiniuConfig config);

    /**
     * 同步数据
     *
     * @param config 配置
     */
    void synchronize(QiniuConfig config);

    /**
     * 查询文件
     *
     * @param query 数据查询对象
     * @return
     */
    List<QiniuContent> listContent(QiniuConfigQuery query);

    /**
     * 根据 contentId 获取
     *
     * @param contentId 文件ID
     * @return /
     */
    QiniuContent getByContentId(Long contentId);

    /**
     * 下载文件
     *
     * @param content 文件信息
     * @param config  配置
     * @return String
     */
    String download(QiniuContent content, QiniuConfig config);

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<QiniuContent> queryAll) throws IOException;
}
