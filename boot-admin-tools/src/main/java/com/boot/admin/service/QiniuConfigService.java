package com.boot.admin.service;

import com.boot.admin.core.service.Service;
import com.boot.admin.model.QiniuConfig;

/**
 * 七牛云存储 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface QiniuConfigService extends Service<QiniuConfig> {

    /**
     * 修改 七牛云存储配置
     *
     * @param resource 实体对象
     */
    void updateQiniuConfig(QiniuConfig resource);

    /**
     * 查询 七牛云存储配置
     *
     * @return 实体对象
     */
    QiniuConfig getQiniuConfig();

    /**
     * 同步数据
     *
     * @param config 配置
     */
    void synchronize(QiniuConfig config);
}
