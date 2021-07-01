package org.utility.service.dto;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Li Yanfeng
 * @since 2021-04-15
 */
@ApiModel(description = "日志数据传输对象")
public class LogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;
    /**
     * 方法名
     */
    private String method;
    /**
     * 参数
     */
    private String params;
    /**
     * 日志类型
     */
    private String logType;
    /**
     * 请求Ip
     */
    private String requestIp;
    /**
     * 请求耗时
     */
    private Long time;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 地址
     */
    private String address;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 描述
     */
    private String description;
    /**
     * 异常详细
     */
    private byte[] exceptionDetail;
    /**
     * 创建时间
     */
    protected Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public byte[] getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(byte[] exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "LogDTO{" +
                "id=" + id +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", logType='" + logType + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", time=" + time +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", browser='" + browser + '\'' +
                ", description='" + description + '\'' +
                ", exceptionDetail=" + Arrays.toString(exceptionDetail) +
                ", createTime=" + createTime +
                '}';
    }
}
