package org.utility.modules.system.service;

import org.utility.model.vo.EmailVO;

/**
 * 验证码 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface VerifyService {

    /**
     * 发送验证码
     *
     * @param email /
     * @param key   /
     * @return /
     */
    EmailVO sendEmail(String email, String key);


    /**
     * 验证
     *
     * @param code /
     * @param key  /
     */
    void validated(String key, String code);
}
