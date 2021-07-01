package org.utility.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 邮件配置，数据存覆盖式存入数据存
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@TableName(value = "tool_email_config")
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private Long configId;

    @ApiModelProperty(value = "邮件服务器 SMTP 地址")
    @NotBlank
    private String host;

    @ApiModelProperty(value = "邮件服务器 SMTP 端口")
    @NotBlank
    private String port;

    @ApiModelProperty(value = "发件者用户名")
    @NotBlank
    private String user;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String pass;

    @ApiModelProperty(value = "收件人")
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
