package com.boot.admin.config;

import com.boot.admin.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限配置
 *
 * @author Li Yanfeng
 */
@Component(value = "authorize")
public class PermissionConfig {

    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> permissionsList =
            SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return permissionsList.contains("admin") || Arrays.stream(permissions).anyMatch(permissionsList::contains);
    }
}
