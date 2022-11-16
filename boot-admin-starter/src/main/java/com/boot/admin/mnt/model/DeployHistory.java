package com.boot.admin.mnt.model;

import com.baomidou.mybatisplus.annotation.*;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "部署历史")
@TableName(value = "mnt_deploy_history")
public class DeployHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = ValidGroup.Update.class)
    private Long historyId;

    @Schema(description = "部署编号")
    private Long deployId;

    @Schema(description = "服务器IP")
    private String ip;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @Schema(description = "创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String createByName;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getDeployId() {
        return deployId;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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
}
