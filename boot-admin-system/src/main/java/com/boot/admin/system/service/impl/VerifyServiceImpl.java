package com.boot.admin.system.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.model.vo.EmailVO;
import com.boot.admin.system.service.VerifyService;
import com.boot.admin.util.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 验证码 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
public class VerifyServiceImpl implements VerifyService {

    @Value("${code.expiration}")
    private Long expiration;

    private final RedisUtils redisUtils;

    public VerifyServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }


    @Override
    public EmailVO sendEmail(String email, String key) {
        EmailVO emailVo;
        String content;
        String redisKey = key + email;
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template",
            TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        Object oldCode = redisUtils.get(redisKey);
        if (ObjectUtil.isNull(oldCode)) {
            String code = RandomUtil.randomNumbers(6);
            // 存入缓存
            if (!redisUtils.set(redisKey, code, expiration)) {
                throw new BadRequestException("服务异常，请联系网站负责人");
            }
            content = template.render(Dict.create().set("code", code));
            emailVo = new EmailVO(Collections.singletonList(email), "ADMIN后台管理系统", content);
        } else {
            // 存在就再次发送原来的验证码
            content = template.render(Dict.create().set("code", oldCode));
            emailVo = new EmailVO(Collections.singletonList(email), "ADMIN后台管理系统", content);
        }
        return emailVo;
    }

    @Override
    public void validated(String key, String code) {
        Object value = redisUtils.get(key);
        if (ObjectUtil.isNull(value) || ObjectUtil.notEqual(value.toString(), code)) {
            throw new BadRequestException("无效验证码");
        } else {
            redisUtils.del(key);
        }
    }
}
