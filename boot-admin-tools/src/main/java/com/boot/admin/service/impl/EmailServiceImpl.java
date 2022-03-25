package com.boot.admin.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mapper.EmailConfigMapper;
import com.boot.admin.model.EmailConfig;
import com.boot.admin.model.vo.EmailVO;
import com.boot.admin.service.EmailService;
import com.boot.admin.util.EncryptUtils;
import com.boot.admin.util.RedisUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮件配置 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"email"})
public class EmailServiceImpl extends ServiceImpl<EmailConfigMapper, EmailConfig> implements EmailService {

    private final RedisUtils redisUtils;

    public EmailServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateEmailConfig(EmailConfig resource, EmailConfig old) throws Exception {
        resource.setConfigId(1L);
        if (!resource.getPass().equals(old.getPass())) {
            // 对称加密
            resource.setPass(EncryptUtils.desEncrypt(resource.getPass()));
        }
        baseMapper.updateById(resource);
        // 更新缓存
        redisUtils.set(CacheKey.EMAIL_CONFIG, resource);
    }

    @Cacheable(key = "'config'")
    @Override
    public EmailConfig getEmailConfig() {
        return baseMapper.selectById(1L);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void send(EmailVO emailVo, EmailConfig emailConfig) {
        if (emailConfig == null || emailConfig.getConfigId() == null) {
            throw new BadRequestException("请先配置，再操作");
        }
        // 封装
        MailAccount account = new MailAccount();
        account.setUser(emailConfig.getUser());
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        try {
            // 对称解密
            account.setPass(EncryptUtils.desDecrypt(emailConfig.getPass()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setFrom(emailConfig.getFromUser());
        // ssl方式发送
        account.setSslEnable(true);
        // 使用STARTTLS安全连接
        account.setStarttlsEnable(true);
        String content = emailVo.getContent();
        // 发送
        try {
            int size = emailVo.getTos().size();
            Mail.create(account)
                .setTos(emailVo.getTos().toArray(new String[size]))
                .setTitle(emailVo.getSubject())
                .setContent(content)
                .setHtml(true)
                //关闭session
                .setUseGlobalSession(false)
                .send();
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
