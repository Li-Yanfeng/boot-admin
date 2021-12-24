package com.boot.admin.mnt.websocket;

/**
 * 消息类型枚举
 *
 * @author Li Yanfeng
 */
public enum MsgType {

    /**
     * 连接
     */
    CONNECT,
    /**
     * 关闭
     */
    CLOSE,
    /**
     * 信息
     */
    INFO,
    /**
     * 错误
     */
    ERROR
}
