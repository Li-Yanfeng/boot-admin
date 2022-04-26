package com.boot.admin.mnt.service.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.boot.admin.core.service.dto.BaseDTO;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "部署 数据传输对象")
public class DeployDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(groups = ValidGroup.Update.class)
    private Long deployId;

    @ApiModelProperty(value = "应用编号")
    private Long appId;


    @ApiModelProperty(value = "应用管理")
    private AppDTO app;

    @ApiModelProperty(value = "服务器")
    private List<ServerDTO> deploys;

    @ApiModelProperty(value = "服务器名称")
    private String servers;

    @ApiModelProperty(value = "服务状态")
    private String status;

    public String getServers() {
        if (CollectionUtil.isNotEmpty(deploys)) {
            return deploys.stream().map(ServerDTO::getName).collect(Collectors.joining(","));
        }
        return servers;
    }


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

    public AppDTO getApp() {
        return app;
    }

    public void setApp(AppDTO app) {
        this.app = app;
    }

    public List<ServerDTO> getDeploys() {
        return deploys;
    }

    public void setDeploys(List<ServerDTO> deploys) {
        this.deploys = deploys;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeployDTO deployDTO = (DeployDTO) o;
        return Objects.equals(deployId, deployDTO.deployId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deployId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
