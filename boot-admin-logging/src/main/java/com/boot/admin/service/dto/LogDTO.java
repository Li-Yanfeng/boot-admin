package com.boot.admin.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 */
@Schema(description = "日志 数据传输对象")
public class LogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long logId;

    @Schema(description = "方法名")
    private String method;

    @Schema(description = "参数")
    private String params;

    @Schema(description = "日志类型")
    private String logType;

    @Schema(description = "请求Ip")
    private String requestIp;

    @Schema(description = "请求耗时")
    private Long time;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "异常详细")
    private byte[] exceptionDetail;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "创建人名称")
    private String createByName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;


    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
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

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(byte[] exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
