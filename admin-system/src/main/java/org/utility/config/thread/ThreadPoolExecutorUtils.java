package org.utility.config.thread;

import org.utility.util.SpringContextHolder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于获取自定义线程池
 *
 * @author Li Yanfeng
 * @link https://juejin.im/entry/5abb8f6951882555677e9da2
 */
public class ThreadPoolExecutorUtils {

    /**
     * 获取线程池
     *
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getPool() {
        AsyncTaskProperties properties = SpringContextHolder.getBean(AsyncTaskProperties.class);
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(properties.getQueueCapacity()),
                new TheadFactoryName()
        );
    }
}
