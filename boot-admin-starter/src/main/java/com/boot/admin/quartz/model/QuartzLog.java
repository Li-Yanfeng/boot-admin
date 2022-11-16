package com.boot.admin.quartz.model;

import com.baomidou.mybatisplus.annotation.*;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "定时任务日志")
@TableName(value = "sys_quartz_log")
public class QuartzLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long logId;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "Bean名称")
    private String beanName;

    @Schema(description = "方法名称")
    private String methodName;

    @Schema(description = "参数")
    private String params;

    @Schema(description = "cron表达式")
    private String cronExpression;

    @Schema(description = "是否成功")
    @TableField(value = "is_success")
    private Integer success;

    @Schema(description = "异常详情")
    private String exceptionDetail;

    @Schema(description = "执行耗时")
    private Long time;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(String exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime  createTime) {
        this.createTime = createTime;
    }
}
