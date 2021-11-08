package org.utility.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utility.annotation.Log;
import org.utility.annotation.rest.AnonymousDeleteMapping;
import org.utility.annotation.rest.AnonymousGetMapping;
import org.utility.annotation.rest.AnonymousPostMapping;
import org.utility.api.Result;
import org.utility.config.RsaProperties;
import org.utility.exception.BadRequestException;
import org.utility.exception.enums.UserErrorCode;
import org.utility.modules.security.config.bean.LoginCodeEnum;
import org.utility.modules.security.config.bean.LoginProperties;
import org.utility.modules.security.config.bean.SecurityProperties;
import org.utility.modules.security.security.TokenProvider;
import org.utility.modules.security.service.OnlineUserService;
import org.utility.modules.security.service.dto.AuthUserDTO;
import org.utility.modules.security.service.dto.JwtUserDTO;
import org.utility.util.RedisUtils;
import org.utility.util.RsaUtils;
import org.utility.util.SecurityUtils;

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
    public Result login(@Validated @RequestBody AuthUserDTO authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StrUtil.isBlank(code)) {
            throw new BadRequestException(UserErrorCode.USER_VERIFICATION_CODE_DOES_NOT_EXIST_OR_EXPIRED);
        }
        if (StrUtil.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException(UserErrorCode.USER_VERIFICATION_CODE_ERROR);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
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
        return Result.success(authInfo);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "/info")
    public Result getUserInfo() {
        return Result.success(SecurityUtils.getCurrentUser());
    }

    @ApiOperation(value = "获取验证码")
    @AnonymousGetMapping(value = "/code")
    public Result getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = securityProperties.getCodeKey() + IdUtil.simpleUUID();
        // 当验证码类型为 arithmetic 且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
//            put("img", captcha.toBase64());
            put("img", captcha.text());
            put("uuid", uuid);
        }};
        return Result.success(imgResult);
    }

    @ApiOperation(value = "退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public Result logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return Result.success();
    }
}
