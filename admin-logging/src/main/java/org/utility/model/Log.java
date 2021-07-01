package org.utility.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志
 *
 * @author Li Yanfeng
 * @since 2021-04-15
 */
@TableName(value = "sys_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "方法名")
    private String method;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "日志类型")
    private String logType;

    @ApiModelProperty(value = "请求Ip")
    private String requestIp;

    @ApiModelProperty(value = "请求耗时")
    private Long time;

    @ApiModelProperty(value = "操作用户")
    private String username;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "浏览器")
    private String browser;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "异常详细")
    private byte[] exceptionDetail;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
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

    public Log(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
