package org.utility.modules.security.service.dto;

import java.util.Date;

/**
 * 在线用户
 *
 * @author Li Yanfeng
 */
public class OnlineUserDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 岗位
     */
    private String dept;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * IP
     */
    private String ip;

    /**
     * 地址
     */
    private String address;

    /**
     * token
     */
    private String key;

    /**
     * 登录时间
     */
    private Date loginTime;


    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDept() {
        return this.dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getBrowser() {
        return this.browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public OnlineUserDTO() {
    }

    public OnlineUserDTO(String userName, String nickName, String dept, String browser, String ip, String address, String key, Date loginTime) {
        this.userName = userName;
        this.nickName = nickName;
        this.dept = dept;
        this.browser = browser;
        this.ip = ip;
        this.address = address;
        this.key = key;
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "OnlineUserDTO{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", dept='" + dept + '\'' +
                ", browser='" + browser + '\'' +
                ", ip='" + ip + '\'' +
                ", address='" + address + '\'' +
                ", key='" + key + '\'' +
                ", loginTime=" + loginTime +
                '}';
    }
}
