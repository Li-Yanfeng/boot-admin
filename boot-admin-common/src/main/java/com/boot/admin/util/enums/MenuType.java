package com.boot.admin.util.enums;

/**
 * 菜单类型：{目录、菜单、按钮}
 *
 * @author Li Yanfeng
 */
public enum MenuType {

    FOLDER(0, "目录"),

    MENU(1, "菜单"),

    BUTTON(2, "按钮");

    /**
     * Value
     */
    private final Integer value;
    /**
     * 描述
     */
    private final String description;

    public static MenuType find(Integer code) {
        for (MenuType value : MenuType.values()) {
            if (value.getValue().equals(code)) {
                return value;
            }
        }
        return null;
    }


    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    MenuType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
