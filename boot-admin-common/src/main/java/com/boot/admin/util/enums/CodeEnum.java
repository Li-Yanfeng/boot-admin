package com.boot.admin.util.enums;

/**
 * 验证码业务场景对应的 Redis 中的 key
 *
 * @author Li Yanfeng
 */
public enum CodeEnum {

    PHONE_RESET_EMAIL_CODE("phone_reset_email_code_", "通过手机号码重置邮箱"),

    EMAIL_RESET_EMAIL_CODE("email_reset_email_code_", "通过旧邮箱重置邮箱"),

    PHONE_RESET_PWD_CODE("phone_reset_pwd_code_", "通过手机号码重置密码"),

    EMAIL_RESET_PWD_CODE("email_reset_pwd_code_", "通过邮箱重置密码");

    /**
     * 业务状态码
     */
    private final String key;
    /**
     * 描述
     */
    private final String description;


    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    CodeEnum(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
