package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "邮件配置")
@TableName(value = "tool_email_config")
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = ValidGroup.Update.class)
    private Long configId;

    @Schema(description = "邮件服务器 SMTP 地址")
    @NotBlank
    private String host;

    @Schema(description = "邮件服务器 SMTP 端口")
    @NotBlank
    private String port;

    @Schema(description = "邮箱地址")
    @NotBlank
    private String user;

    @Schema(description = "授权密码")
    @NotBlank
    private String pass;

    @Schema(description = "发件人昵称（遵循RFC-822标准）")
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
