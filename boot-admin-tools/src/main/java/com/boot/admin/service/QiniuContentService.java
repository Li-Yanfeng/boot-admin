package com.boot.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.Service;
import com.boot.admin.model.QiniuConfig;
import com.boot.admin.model.QiniuContent;
import com.boot.admin.service.dto.QiniuContentQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 七牛云文件 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface QiniuContentService extends Service<QiniuContent> {

    /**
     * 根据 ID 批量删除
     *
     * @param ids    主键ID列表
     * @param config 七牛云文件配置
     */
    void removeQiniuContentByIds(Collection<Long> ids, QiniuConfig config);

    /**
     * 根据 query 条件查询
     *
     * @param query 数据查询对象
     * @return 列表查询结果
     */
    List<QiniuContent> listQiniuContents(QiniuContentQuery query);

    /**
     * 根据 query 条件翻页查询
     *
     * @param query 数据查询对象
     * @param page  翻页查询对象
     * @return 翻页查询结果
     */
    Page<QiniuContent> listQiniuContents(QiniuContentQuery query, Page<QiniuContent> page);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体对象
     */
    QiniuContent getQiniuContentById(Long id);

    /**
     * 上传文件
     *
     * @param file   文件
     * @param config 配置
     * @return 实体对象
     */
    QiniuContent uploadContent(MultipartFile file, QiniuConfig config);

    /**
     * 下载文件
     *
     * @param content 文件信息
     * @param config  七牛云文件配置
     * @return 下载地址
     */
    String downloadContent(QiniuContent content, QiniuConfig config);

    /**
     * 导出数据
     *
     * @param exportData 待导出数据
     * @param response   响应对象
     * @throws IOException /
     */
    void exportQiniuContent(List<QiniuContent> exportData, HttpServletResponse response) throws IOException;
}
