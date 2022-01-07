package com.boot.admin.security.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.security.config.bean.SecurityProperties;
import com.boot.admin.security.service.dto.JwtUserDTO;
import com.boot.admin.security.service.dto.OnlineUserDTO;
import com.boot.admin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 在线用户服务
 *
 * @author Li Yanfeng
 */
@Service
public class OnlineUserService {

    private static final Logger logger = LoggerFactory.getLogger(OnlineUserService.class);

    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;

    public OnlineUserService(SecurityProperties securityProperties, RedisUtils redisUtils) {
        this.securityProperties = securityProperties;
        this.redisUtils = redisUtils;
    }

    /**
     * 保存在线用户信息
     *
     * @param jwtUserDto /
     * @param token      /
     * @param request    /
     */
    public void save(JwtUserDTO jwtUserDto, String token, HttpServletRequest request) {
        String dept = jwtUserDto.getUser().getDept().getName();
        String ip = IpUtils.getIp(request);
        String browser = RequestHolder.getBrowser(request);
        String address = IpUtils.getCityInfo(ip);
        OnlineUserDTO onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDTO(jwtUserDto.getUsername(), jwtUserDto.getUser().getNickName(), dept,
                browser, ip, address, EncryptUtils.desEncrypt(token), LocalDateTime.now());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        redisUtils.set(securityProperties.getOnlineKey() + token, onlineUserDto,
            securityProperties.getTokenValidityInSeconds() / 1000);
    }

    /**
     * 翻页查询
     *
     * @param filter /
     * @param page   /
     * @return /
     */
    public Page<OnlineUserDTO> list(String filter, Page<?> page) {
        List<OnlineUserDTO> onlineUsers = list(filter);
        return PageUtils.toPage(PageUtils.toPage(page.getCurrent(), page.getSize(), onlineUsers), onlineUsers.size());
    }

    /**
     * 查询
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUserDTO> list(String filter) {
        List<String> keys = redisUtils.scan(securityProperties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<OnlineUserDTO> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            OnlineUserDTO onlineUserDto = (OnlineUserDTO) redisUtils.get(key);
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUserDto.toString().contains(filter)) {
                    onlineUserDtos.add(onlineUserDto);
                }
            } else {
                onlineUserDtos.add(onlineUserDto);
            }
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtos;
    }

    /**
     * 踢出用户
     *
     * @param key /
     */
    public void kickOut(String key) {
        key = securityProperties.getOnlineKey() + key;
        redisUtils.del(key);
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        String key = securityProperties.getOnlineKey() + token;
        redisUtils.del(key);
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) {
        List<OnlineUserDTO> onlineUsers = list(username);
        for (OnlineUserDTO onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                kickOut(onlineUser.getKey());
            }
        }
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public OnlineUserDTO getOne(String key) {
        return (OnlineUserDTO) redisUtils.get(key);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<OnlineUserDTO> onlineUserDtos = list(userName);
        if (onlineUserDtos == null || onlineUserDtos.isEmpty()) {
            return;
        }
        for (OnlineUserDTO onlineUserDto : onlineUserDtos) {
            if (onlineUserDto.getUserName().equals(userName)) {
                try {
                    String token = EncryptUtils.desDecrypt(onlineUserDto.getKey());
                    if (StrUtil.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                        kickOut(token);
                    } else if (StrUtil.isBlank(igoreToken)) {
                        kickOut(token);
                    }
                } catch (Exception e) {
                    logger.error("checkUser is error", e);
                }
            }
        }
    }

    /**
     * 导出数据
     *
     * @param exportData /
     * @param response   /
     * @throws IOException /
     */
    public void export(List<OnlineUserDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUserDTO user : exportData) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("部门", user.getDept());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
