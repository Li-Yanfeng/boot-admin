package org.utility.service;

import org.utility.core.service.Service;
import org.utility.model.EmailConfig;
import org.utility.model.vo.EmailVO;

/**
 * 邮件配置 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface EmailService extends Service<EmailConfig> {

    /**
     * 更新 邮件配置
     *
     * @param resource 邮件配置
     * @param old      旧的邮件配置
     * @throws Exception /
     */
    void updateEmailConfig(EmailConfig resource, EmailConfig old) throws Exception;

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    EmailConfig getEmailConfig();

    /**
     * 发送邮件
     *
     * @param emailVo     邮件发送的内容
     * @param emailConfig 邮件配置
     */
    void send(EmailVO emailVo, EmailConfig emailConfig);
}
