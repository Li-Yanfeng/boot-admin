package com.boot.admin.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步锁 工具类
 *
 * @author Li Yanfeng
 */
@Component
public class SynchronizedUtils {

    Map<String, ReentrantLock> mutexCache = new ConcurrentHashMap<>();

    /**
     * 执行
     *
     * @param key       唯一标识
     * @param statement 待执行的业务
     */
    public void exec(String key, Runnable statement) {
        // 获取锁对象
        ReentrantLock mutex4Key = null;
        ReentrantLock mutexInCache;
        do {
            // 再次执行需要先释放锁
            if (mutex4Key != null) {
                mutex4Key.unlock();
            }

            // 创建锁
            mutex4Key = mutexCache.computeIfAbsent(key, lock -> new ReentrantLock());

            mutex4Key.lock();
            // 再次获取锁
            mutexInCache = mutexCache.get(key);

            /*
             * 需要判断：
             * 1.锁是否被删除
             * 2.是否同一把锁
             */
        } while (mutexInCache == null || mutex4Key != mutexInCache);

        try {
            // 执行业务
            statement.run();
        } finally {
            // 线程等待数为 0 时候从缓存中移除 锁对象
            if (mutex4Key.getQueueLength() == 0) {
                mutexCache.remove(key);
            }
            // 释放锁
            mutex4Key.unlock();
        }
    }
}
