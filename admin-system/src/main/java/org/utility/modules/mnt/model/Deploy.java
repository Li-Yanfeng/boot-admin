package org.utility.modules.mnt.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import org.utility.core.model.BaseEntity;

import java.io.Serializable;

/**
 * 部署管理
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@TableName(value = "mnt_deploy")
public class Deploy extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "deploy_id", type = IdType.ASSIGN_ID)
    private Long deployId;

    @ApiModelProperty(value = "应用编号")
    private Long appId;


    public Long getDeployId() {
        return deployId;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
