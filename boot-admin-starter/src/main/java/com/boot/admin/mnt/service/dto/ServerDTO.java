package com.boot.admin.mnt.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "服务器 数据传输对象")
public class ServerDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long serverId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;


    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ServerDTO serverDTO = (ServerDTO) o;
        return Objects.equals(serverId, serverDTO.serverId) && Objects.equals(name, serverDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
