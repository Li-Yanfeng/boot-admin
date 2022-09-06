package com.boot.admin.mnt.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@ApiModel(description = "服务器")
@TableName(value = "mnt_deploy_server")
public class DeployServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部署ID")
    @TableField
    private Long deployId;

    @ApiModelProperty(value = "服务ID")
    @TableField
    private Long serverId;


    public Long getDeployId() {
        return deployId;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }
}
