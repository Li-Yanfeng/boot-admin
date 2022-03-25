package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.core.validation.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "邮件配置")
@TableName(value = "tool_email_config")
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = Update.class)
    private Long configId;

    @ApiModelProperty(value = "邮件服务器 SMTP 地址")
    @NotBlank
    private String host;

    @ApiModelProperty(value = "邮件服务器 SMTP 端口")
    @NotBlank
    private String port;

    @ApiModelProperty(value = "邮箱地址")
    @NotBlank
    private String user;

    @ApiModelProperty(value = "授权密码")
    @NotBlank
    private String pass;

    @ApiModelProperty(value = "发件人昵称（遵循RFC-822标准）")
    @NotBlank
    private String fromUser;


    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
