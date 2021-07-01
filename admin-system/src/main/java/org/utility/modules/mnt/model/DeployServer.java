package org.utility.modules.mnt.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 应用与服务器关联
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@TableName(value = "mnt_deploy_server")
public class DeployServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部署ID")
    @TableField(value = "deploy_id")
    private Long deployId;

    @ApiModelProperty(value = "服务ID")
    @TableField(value = "server_id")
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
