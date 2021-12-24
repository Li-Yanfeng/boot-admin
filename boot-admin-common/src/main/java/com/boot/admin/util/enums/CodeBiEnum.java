package com.boot.admin.util.enums;

/**
 * 验证码业务场景
 *
 * @author Li Yanfeng
 */
public enum CodeBiEnum {

    ONE(1, "旧邮箱修改邮箱"),

    TWO(2, "通过邮箱修改密码");

    /**
     * 业务状态码
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String description;

    public static CodeBiEnum find(Integer code) {
        for (CodeBiEnum value : CodeBiEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }


    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    CodeBiEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
