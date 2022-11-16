package com.boot.admin.security.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 */
@Schema(description = "在线用户 数据传输对象")
public class OnlineUserDTO {

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "部门")
    private String dept;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "token")
    private String key;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;


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

    public LocalDateTime getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(LocalDateTime  loginTime) {
        this.loginTime = loginTime;
    }

    public OnlineUserDTO() {
    }

    public OnlineUserDTO(String userName, String nickName, String dept, String browser, String ip, String address,
                         String key, LocalDateTime loginTime) {
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
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
