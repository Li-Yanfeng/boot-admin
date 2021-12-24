package com.boot.admin.system.service;

import com.boot.admin.model.vo.EmailVO;

/**
 * 验证码 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface VerifyService {

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param key   /
     * @return /
     */
    EmailVO sendEmail(String email, String key);

    /**
     * 验证
     *
     * @param key  /
     * @param code 验证码
     */
    void validated(String key, String code);
}
