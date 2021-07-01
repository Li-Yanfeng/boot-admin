package org.utility.modules.mnt.service.dto;


import java.io.Serializable;
import java.util.Date;

/**
 * 部署历史管理 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class DeployHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long historyId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 部署日期
     */
    private Date deployDate;
    /**
     * 部署用户
     */
    private String deployUser;
    /**
     * 服务器IP
     */
    private String ip;
    /**
     * 部署编号
     */
    private Long deployId;


    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(Date deployDate) {
        this.deployDate = deployDate;
    }

    public String getDeployUser() {
        return deployUser;
    }

    public void setDeployUser(String deployUser) {
        this.deployUser = deployUser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getDeployId() {
        return deployId;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    @Override
    public String toString() {
        return "DeployHistoryDTO{" +
                "historyId=" + historyId +
                ", appName='" + appName + '\'' +
                ", deployDate=" + deployDate +
                ", deployUser='" + deployUser + '\'' +
                ", ip='" + ip + '\'' +
                ", deployId=" + deployId +
                '}';
    }
}
