package org.utility.modules.security.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.utility.modules.system.service.dto.UserDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Li Yanfeng
 */
public class JwtUserDTO implements UserDetails {

    private final UserDTO user;

    private final List<Long> dataScopes;

    @JSONField(serialize = false)
    private final List<GrantedAuthority> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JSONField(serialize = false)
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

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return user.getEnabled();
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
        return "JwtUserDTO{" +
                "user=" + user +
                ", dataScopes=" + dataScopes +
                ", authorities=" + authorities +
                '}';
    }
}
