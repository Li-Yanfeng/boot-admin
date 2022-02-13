package com.boot.admin.util.enums;

/**
 * 数据权限字段枚举
 *
 * @author Li Yanfeng
 */
public enum DataScopeFieldEnum {

    // 根据创建者
    CREATE_BY("create_by"),

    // 根据部门ID
    DEPT_ID("dept_id");

    /**
     * Value
     */
    private final String value;


    public String getValue() {
        return value;
    }

    DataScopeFieldEnum(String value) {
        this.value = value;
    }
}
