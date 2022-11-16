package com.boot.admin.mnt.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.core.model.BaseEntity;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "服务器")
@TableName(value = "mnt_server")
public class Server extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
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
}
