package com.boot.admin.mnt.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.core.validation.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "部署历史")
@TableName(value = "mnt_deploy_history")
public class DeployHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = Update.class)
    private Long historyId;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "部署日期")
    private LocalDateTime deployDate;

    @ApiModelProperty(value = "部署用户")
    private String deployUser;

    @ApiModelProperty(value = "服务器IP")
    private String ip;

    @ApiModelProperty(value = "部署编号")
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

    public LocalDateTime getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(LocalDateTime  deployDate) {
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
}
