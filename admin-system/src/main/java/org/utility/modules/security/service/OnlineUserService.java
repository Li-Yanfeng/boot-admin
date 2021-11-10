package org.utility.modules.security.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.utility.core.service.dto.BaseQuery;
import org.utility.modules.security.config.bean.SecurityProperties;
import org.utility.modules.security.service.dto.JwtUserDTO;
import org.utility.modules.security.service.dto.OnlineUserDTO;
import org.utility.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 在线用户服务
 *
 * @author Li Yanfeng
 */
@Service
public class OnlineUserService {

    private static final Logger logger = LoggerFactory.getLogger(OnlineUserService.class);

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;

    public OnlineUserService(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
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
                    browser, ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        redisUtils.set(properties.getOnlineKey() + token, onlineUserDto, properties.getTokenValidityInSeconds() / 1000);
    }

    /**
     * 翻页查询
     *
     * @param filter /
     * @param query  /
     * @return /
     */
    public IPage<?> page(String filter, BaseQuery query) {
        List<OnlineUserDTO> onlineUserDtos = this.list(filter);
        return PageUtils.toPage(query.getCurrent(), query.getSize(), onlineUserDtos);
    }

    /**
     * 查询
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUserDTO> list(String filter) {
        List<String> keys = redisUtils.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<OnlineUserDTO> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            OnlineUserDTO onlineUserDto = (OnlineUserDTO) redisUtils.get(key);
            if (StrUtil.isNotBlank(filter)) {
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
        key = properties.getOnlineKey() + key;
        redisUtils.del(key);
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        String key = properties.getOnlineKey() + token;
        redisUtils.del(key);
    }

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    public void download(HttpServletResponse response, List<OnlineUserDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUserDTO user : queryAll) {
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
        List<OnlineUserDTO> onlineUserDtos = this.list(userName);
        if (onlineUserDtos == null || onlineUserDtos.isEmpty()) {
            return;
        }
        for (OnlineUserDTO onlineUserDto : onlineUserDtos) {
            if (onlineUserDto.getUserName().equals(userName)) {
                try {
                    String token = EncryptUtils.desDecrypt(onlineUserDto.getKey());
                    if (StrUtil.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                        this.kickOut(token);
                    } else if (StrUtil.isBlank(igoreToken)) {
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    logger.error("checkUser is error", e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) {
        List<OnlineUserDTO> onlineUsers = this.list(username);
        for (OnlineUserDTO onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                kickOut(onlineUser.getKey());
            }
        }
    }
}
