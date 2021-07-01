package org.utility.modules.system.service;

import java.util.Map;

/**
 * 服务监控 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface MonitorService {

    /**
     * 获取服务器信息
     *
     * @return Map<String, Object>
     */
    Map<String, Object> getServerInfo();
}
