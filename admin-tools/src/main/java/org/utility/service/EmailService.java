package org.utility.service;

import org.utility.model.EmailConfig;
import org.utility.model.vo.EmailVO;

/**
 * 邮件配置 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public interface EmailService {

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮箱配置
     * @param old         /
     * @return /
     * @throws Exception /
     */
    EmailConfig config(EmailConfig emailConfig, EmailConfig old) throws Exception;

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    EmailConfig getConfig();

    /**
     * 发送邮件
     *
     * @param emailVo     邮件发送的内容
     * @param emailConfig 邮件配置
     * @throws Exception /
     */
    void send(EmailVO emailVo, EmailConfig emailConfig);
}
