package com.boot.admin.util.enums;

/**
 * 数据权限枚举
 *
 * @author Li Yanfeng
 */
public enum DataScopeEnum {

    ALL("全部", "全部数据权限"),

    ORG("本级", "本级数据权限"),

    ORG_AND_CHILD("本级及以下", "本级及以下数据权限"),

    SELF("自己", "仅自己数据权限"),

    CUSTOM("自定义", "自定义数据权限");

    /**
     * Value
     */
    private final String value;
    /**
     * 描述
     */
    private final String description;

    public static DataScopeEnum find(String val) {
        for (DataScopeEnum dataScopeEnum : DataScopeEnum.values()) {
            if (val.equals(dataScopeEnum.getValue())) {
                return dataScopeEnum;
            }
        }
        return null;
    }


    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    DataScopeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
