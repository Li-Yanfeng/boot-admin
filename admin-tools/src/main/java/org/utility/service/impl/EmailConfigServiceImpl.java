package org.utility.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.exception.BadRequestException;
import org.utility.mapper.EmailMapper;
import org.utility.model.EmailConfig;
import org.utility.model.vo.EmailVO;
import org.utility.service.EmailService;
import org.utility.util.EncryptUtils;

/**
 * 邮件配置 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Service
@CacheConfig(cacheNames = {"email"})
public class EmailConfigServiceImpl implements EmailService {

    private final EmailMapper emailMapper;

    public EmailConfigServiceImpl(EmailMapper emailMapper) {
        this.emailMapper = emailMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "'config'")
    @Override
    public EmailConfig config(EmailConfig emailConfig, EmailConfig old) throws Exception {
        emailConfig.setConfigId(1L);
        if (!emailConfig.getPass().equals(old.getPass())) {
            // 对称加密
            emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
        }
        emailMapper.updateById(emailConfig);
        return emailConfig;
    }

    @Cacheable(key = "'config'")
    @Override
    public EmailConfig getConfig() {
        return emailMapper.selectById(1L);
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
        account.setFrom(emailConfig.getUser() + "<" + emailConfig.getFromUser() + ">");
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
