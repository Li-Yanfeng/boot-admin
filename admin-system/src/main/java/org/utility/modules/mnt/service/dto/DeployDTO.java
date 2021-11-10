package org.utility.modules.mnt.service.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部署管理 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class DeployDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long deployId;
    /**
     * 应用编号
     */
    private Long appId;

    /**
     * 应用管理
     */
    private AppDTO app;
    /**
     * 服务器
     */
    private Set<ServerDTO> deploys;
    /**
     * 服务器名称
     */
    private String servers;
    /**
     * 服务状态
     */
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

    public Set<ServerDTO> getDeploys() {
        return deploys;
    }

    public void setDeploys(Set<ServerDTO> deploys) {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeployDTO deployDto = (DeployDTO) o;
        return Objects.equals(deployId, deployDto.deployId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deployId);
    }

    @Override
    public String toString() {
        return "DeployDTO{" +
                "deployId=" + deployId +
                ", appId=" + appId +
                ", app=" + app +
                ", deploys=" + deploys +
                ", servers='" + servers + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
