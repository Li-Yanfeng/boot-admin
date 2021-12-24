package com.boot.admin.security.service;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/**
 * 用于清理 用户登录信息缓存，为防止Spring循环依赖与安全考虑 ，单独构成工具类
 *
 * @author Li Yanfeng
 */
@Component
public class UserCacheClean {

    /**
     * 清理特定用户缓存信息, 如用户信息发生变化
     *
     * @param username /
     */
    public void cleanUserCache(String username) {
        if (StrUtil.isNotEmpty(username)) {
            UserDetailsServiceImpl.userDtoCache.remove(username);
        }
    }

    /**
     * 清理所有用户的缓存信息,如发生角色授权信息变化，可以简便的全部失效缓存
     */
    public void cleanAll() {
        UserDetailsServiceImpl.userDtoCache.clear();
    }
}
