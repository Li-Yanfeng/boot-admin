package com.boot.admin.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.boot.admin.constant.SystemConstant;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.util.enums.DataScopeEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 安全框架工具，获取当前登录的用户
 *
 * @author Li Yanfeng
 */
public class SecurityUtils {

    /**
     * 获取当前登录的用户
     *
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(UserErrorCode.USER_LOGIN_HAS_EXPIRED);
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        throw new BadRequestException(UserErrorCode.USER_ACCOUNT_DOES_NOT_EXIST);
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * 获取系统用户ID
     *
     * @return 系统用户ID
     */
    public static Long getCurrentUserId() {
        UserDetails userDetails = getCurrentUser();
        return new JSONObject(new JSONObject(userDetails).get("user")).get("userId", Long.class);
    }

    /**
     * 获取当前用户的数据权限
     *
     * @return /
     */
    public static List<Long> getCurrentUserDataScope() {
        UserDetails userDetails = getCurrentUser();
        JSONArray array = JSONUtil.parseArray(new JSONObject(userDetails).get("dataScopes"));
        return JSONUtil.toList(array, Long.class);
    }

    /**
     * 获取数据权限级别
     *
     * @return 级别
     */
    public static String getDataScopeType() {
        List<Long> dataScopes = getCurrentUserDataScope();
        if (dataScopes.size() != 0) {
            return "";
        }
        return DataScopeEnum.ALL.getValue();
    }

    /**
     * 是否管理员
     *
     * @return 是否admin
     */
    public static Boolean isAdmin() {
        List<String> permissions = getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return permissions.contains(SystemConstant.ADMIN);
    }
}
