package com.boot.admin.security.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.system.service.dto.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 */
@ApiModel(description = "授权用户 数据传输对象")
public class JwtUserDTO implements UserDetails {

    @ApiModelProperty(value = "用户")
    private final UserDTO user;

    @ApiModelProperty(value = "数据范围")
    private final List<Long> dataScopes;

    @ApiModelProperty(value = "权限")
    @JSONField(serialize = false)
    private final List<GrantedAuthority> authorities;


    public Set<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @JSONField(serialize = false)
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return CommonConstant.ENABLE.equals(user.getEnabled());
    }

    public UserDTO getUser() {
        return user;
    }

    public List<Long> getDataScopes() {
        return dataScopes;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public JwtUserDTO(UserDTO user, List<Long> dataScopes, List<GrantedAuthority> authorities) {
        this.user = user;
        this.dataScopes = dataScopes;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
