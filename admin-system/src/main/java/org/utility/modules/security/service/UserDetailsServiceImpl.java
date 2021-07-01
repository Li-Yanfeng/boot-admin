package org.utility.modules.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.utility.exception.BadRequestException;
import org.utility.exception.EntityNotFoundException;
import org.utility.exception.enums.UserErrorCode;
import org.utility.modules.security.config.bean.LoginProperties;
import org.utility.modules.security.service.dto.JwtUserDTO;
import org.utility.modules.system.service.DataService;
import org.utility.modules.system.service.RoleService;
import org.utility.modules.system.service.UserService;
import org.utility.modules.system.service.dto.UserDTO;

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

    public UserDetailsServiceImpl(UserService userService, RoleService roleService, DataService dataService,
                                  LoginProperties loginProperties) {
        this.userService = userService;
        this.roleService = roleService;
        this.dataService = dataService;
        this.loginProperties = loginProperties;
    }

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    @Override
    public JwtUserDTO loadUserByUsername(String username) {
        JwtUserDTO jwtUserDto;
        if (loginProperties.isCacheEnable() && userDtoCache.containsKey(username)) {
            jwtUserDto = userDtoCache.get(username);
            // 检查dataScope是否修改
            List<Long> dataScopes = jwtUserDto.getDataScopes();
            dataScopes.clear();
            dataScopes.addAll(dataService.getDeptIds(jwtUserDto.getUser()));
        } else {
            UserDTO user;
            try {
                user = userService.getByUsername(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException("", e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException(UserErrorCode.USER_ACCOUNT_IS_FROZEN);
                }
                jwtUserDto = new JwtUserDTO(
                        user,
                        dataService.getDeptIds(user),
                        roleService.mapToGrantedAuthorities(user)
                );
                userDtoCache.put(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
