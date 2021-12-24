package com.boot.admin.security.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

/**
 * @author Li Yanfeng
 */
@ApiModel(description = "认证用户 数据传输对象")
public class AuthUserDTO {

    @ApiModelProperty(value = "用户名")
    @NotBlank
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "唯一标识")
    private String uuid = "";


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
