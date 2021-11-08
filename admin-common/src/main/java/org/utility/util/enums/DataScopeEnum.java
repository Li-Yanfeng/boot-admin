package org.utility.util.enums;

/**
 * 数据权限枚举
 *
 * @author Li Yanfeng
 */
public enum DataScopeEnum {

    ALL("全部", "全部的数据权限"),

    THIS_LEVEL("本级", "自己部门的数据权限"),

    CUSTOMIZE("自定义", "自定义的数据权限");

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

    @Override
    public String toString() {
        return "DataScopeEnum{" +
                "value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
