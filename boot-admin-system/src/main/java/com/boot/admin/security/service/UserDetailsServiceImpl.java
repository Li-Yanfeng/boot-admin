package com.boot.admin.security.service;

import com.boot.admin.constant.CommonConstant;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.EntityNotFoundException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.security.config.bean.LoginProperties;
import com.boot.admin.security.service.dto.JwtUserDTO;
import com.boot.admin.system.service.DataService;
import com.boot.admin.system.service.RoleService;
import com.boot.admin.system.service.UserService;
import com.boot.admin.system.service.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户详细信息服务
 *
 * @author Li Yanfeng
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 用户信息缓存
     *
     * @see {@link UserCacheClean}
     */
    static Map<String, JwtUserDTO> userDtoCache = new ConcurrentHashMap<>();

    private final UserService userService;
    private final RoleService roleService;
    private final DataService dataService;
    private final LoginProperties loginProperties;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    public UserDetailsServiceImpl(UserService userService, RoleService roleService, DataService dataService,
                                  LoginProperties loginProperties) {
        this.userService = userService;
        this.roleService = roleService;
        this.dataService = dataService;
        this.loginProperties = loginProperties;
    }

    @Override
    public JwtUserDTO loadUserByUsername(String username) {
        boolean searchDb = true;
        JwtUserDTO jwtUserDTO = null;
        if (loginProperties.isCacheEnable() && userDtoCache.containsKey(username)) {
            jwtUserDTO = userDtoCache.get(username);
            // 检查dataScope是否修改
            List<Long> dataScopes = jwtUserDTO.getDataScopes();
            dataScopes.clear();
            dataScopes.addAll(dataService.listDeptIds(jwtUserDTO.getUser()));
            searchDb = false;
        }
        if (searchDb) {
            UserDTO user;
            try {
                user = userService.loadUserByUsername(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException("", e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("");
            } else {
                if (CommonConstant.UN_ENABLE.equals(user.getEnabled())) {
                    throw new BadRequestException(UserErrorCode.USER_ACCOUNT_IS_FROZEN);
                }
                jwtUserDTO = new JwtUserDTO(
                    user,
                    dataService.listDeptIds(user),
                    roleService.mapToGrantedAuthorities(user)
                );
                userDtoCache.put(username, jwtUserDTO);
            }
        }
        return jwtUserDTO;
    }
}
