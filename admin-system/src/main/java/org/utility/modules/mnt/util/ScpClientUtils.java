package org.utility.modules.mnt.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 远程执行 Linux 命令
 *
 * @author Li Yanfeng
 */
public class ScpClientUtils {

    /**
     * Ip 地址
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    private static Map<String, ScpClientUtils> instance = Maps.newHashMap();

    public static synchronized ScpClientUtils getInstance(String ip, int port, String username, String password) {
        if (instance.get(ip) == null) {
            instance.put(ip, new ScpClientUtils(ip, port, username, password));
        }
        return instance.get(ip);
    }

    public ScpClientUtils(String ip, int port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 获取文件
     *
     * @param remoteFile           远程文件
     * @param localTargetDirectory 本地目标目录
     */
    public void getFile(String remoteFile, String localTargetDirectory) {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (!isAuthenticated) {
                System.err.println("authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            client.get(remoteFile, localTargetDirectory);
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conn.close();
        }
    }

    /**
     * 放置文件
     *
     * @param localFile             远程文件
     * @param remoteTargetDirectory 远程目标目录
     */
    public void putFile(String localFile, String remoteTargetDirectory) {
        this.putFile(localFile, null, remoteTargetDirectory);
    }

    /**
     * 放置文件
     *
     * @param localFile             远程文件
     * @param remoteFileName        远程文件名称
     * @param remoteTargetDirectory 远程目标目录
     */
    public void putFile(String localFile, String remoteFileName, String remoteTargetDirectory) {
        this.putFile(localFile, remoteFileName, remoteTargetDirectory, null);
    }

    /**
     * 放置文件
     *
     * @param localFile             远程文件
     * @param remoteFileName        远程文件名称
     * @param remoteTargetDirectory 远程目标目录
     * @param mode                  模式
     */
    public void putFile(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (!isAuthenticated) {
                System.err.println("authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            if ((mode == null) || (mode.length() == 0)) {
                mode = "0600";
            }
            if (remoteFileName == null) {
                client.put(localFile, remoteTargetDirectory);
            } else {
                client.put(localFile, remoteFileName, remoteTargetDirectory, mode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ScpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conn.close();
        }
    }
}
