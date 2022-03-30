package com.boot.admin.quartz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.model.BaseEntity;
import com.boot.admin.core.validation.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "定时任务")
@TableName(value = "sys_quartz_job")
public class QuartzJob extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String JOB_KEY = "JOB_KEY";

    @ApiModelProperty(value = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long jobId;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "Bean名称")
    private String beanName;

    @ApiModelProperty(value = "方法名称")
    private String methodName;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "cron 表达式")
    private String cronExpression;

    @ApiModelProperty(value = "是否暂停")
    @TableField(value = "is_pause")
    private Integer pause = CommonConstant.NO;

    @ApiModelProperty(value = "子任务ID")
    private String subTask;

    @ApiModelProperty(value = "负责人")
    private String personInCharge;

    @ApiModelProperty(value = "报警邮箱")
    private String email;

    @ApiModelProperty(value = "任务失败后是否暂停")
    private Integer pauseAfterFailure;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "用于子任务唯一标识", hidden = true)
    @TableField(exist = false)
    private String uuid;


    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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

    public Integer getPause() {
        return pause;
    }

    public void setPause(Integer pause) {
        this.pause = pause;
    }

    public String getSubTask() {
        return subTask;
    }

    public void setSubTask(String subTask) {
        this.subTask = subTask;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPauseAfterFailure() {
        return pauseAfterFailure;
    }

    public void setPauseAfterFailure(Integer pauseAfterFailure) {
        this.pauseAfterFailure = pauseAfterFailure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
