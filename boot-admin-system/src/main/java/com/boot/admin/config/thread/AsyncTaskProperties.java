package com.boot.admin.config.thread;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池配置属性类
 *
 * @author Li Yanfeng
 * @link https://juejin.im/entry/5abb8f6951882555677e9da2
 */
@Component
@ConfigurationProperties(prefix = "task.pool")
public class AsyncTaskProperties {

    /**
     * 核心池大小
     */
    private Integer corePoolSize;
    /**
     * 最大池大小
     */
    private Integer maxPoolSize;
    /**
     * 保持活力秒
     */
    private Integer keepAliveSeconds;
    /**
     * 队列能力
     */
    private Integer queueCapacity;


    public Integer getCorePoolSize() {
        return this.corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getKeepAliveSeconds() {
        return this.keepAliveSeconds;
    }

    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public Integer getQueueCapacity() {
        return this.queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}

