package org.utility.modules.mnt.websocket;

/**
 * @author Li Yanfeng
 */
public class SocketMsg {

    /**
     * 消息
     */
    private String msg;
    /**
     * 消息类型
     */
    private MsgType msgType;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public SocketMsg() {
    }

    public SocketMsg(String msg, MsgType msgType) {
        this.msg = msg;
        this.msgType = msgType;
    }
}
