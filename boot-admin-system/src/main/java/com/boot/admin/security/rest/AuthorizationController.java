package com.boot.admin.security.rest;

import cn.hutool.core.util.IdUtil;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.rest.AnonymousDeleteMapping;
import com.boot.admin.annotation.rest.AnonymousGetMapping;
import com.boot.admin.annotation.rest.AnonymousPostMapping;
import com.boot.admin.config.RsaProperties;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.security.config.bean.LoginCodeEnum;
import com.boot.admin.security.config.bean.LoginProperties;
import com.boot.admin.security.config.bean.SecurityProperties;
import com.boot.admin.security.security.TokenProvider;
import com.boot.admin.security.service.OnlineUserService;
import com.boot.admin.security.service.dto.AuthUserDTO;
import com.boot.admin.security.service.dto.JwtUserDTO;
import com.boot.admin.util.RedisUtils;
import com.boot.admin.util.RsaUtils;
import com.boot.admin.util.SecurityUtils;
import com.boot.admin.util.StringUtils;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Li Yanfeng
 */
@Api(tags = "系统：系统授权接口")
@RestController
@RequestMapping(value = "/auth")
@ResultWrapper
public class AuthorizationController {

    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityProperties securityProperties;
    private final LoginProperties loginProperties;
    private final RedisUtils redisUtils;

    public AuthorizationController(OnlineUserService onlineUserService, TokenProvider tokenProvider,
                                   AuthenticationManagerBuilder authenticationManagerBuilder,
                                   SecurityProperties securityProperties, LoginProperties loginProperties,
                                   RedisUtils redisUtils) {
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityProperties = securityProperties;
        this.loginProperties = loginProperties;
        this.redisUtils = redisUtils;
    }


    @ApiOperation(value = "登录授权")
    @Log(value = "用户登录")
    @AnonymousPostMapping(value = "/login")
    public Map<String, Object> login(@Validated @RequestBody AuthUserDTO authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 开启验证码
        if (loginProperties.getLoginCode().getEnabled()) {
            // 查询验证码
            String code = (String) redisUtils.get(authUser.getUuid());
            // 清除验证码
            redisUtils.del(authUser.getUuid());
            if (StringUtils.isBlank(code)) {
                throw new BadRequestException("验证码不存在或已过期");
            }
            if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
                throw new BadRequestException("验证码错误");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUserDTO jwtUserDto = (JwtUserDTO) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", securityProperties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return authInfo;
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "/info")
    public UserDetails getUserInfo() {
        return SecurityUtils.getCurrentUser();
    }

    @ApiOperation(value = "获取验证码")
    @AnonymousGetMapping(value = "/code")
    public Map<String, Object> getCode() {
        if (!loginProperties.getLoginCode().getEnabled()) {
            // 验证码信息
            Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
                put("enabled", CommonConstant.NO);
            }};
            return imgResult;
        }
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = securityProperties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(3) {{
            put("enabled", CommonConstant.YES);
            put("img", captcha.text());
            put("uuid", uuid);
        }};
        return imgResult;
    }

    @ApiOperation(value = "退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public void logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
    }
}
