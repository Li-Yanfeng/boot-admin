package org.utility.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.utility.constant.SystemConstant;
import org.utility.exception.BadRequestException;
import org.utility.exception.EntityNotFoundException;
import org.utility.exception.enums.UserErrorCode;
import org.utility.security.config.bean.LoginProperties;
import org.utility.security.service.dto.JwtUserDTO;
import org.utility.system.service.DataService;
import org.utility.system.service.RoleService;
import org.utility.system.service.UserService;
import org.utility.system.service.dto.UserDTO;

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
                if (SystemConstant.UN_ENABLE.equals(user.getEnabled())) {
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
