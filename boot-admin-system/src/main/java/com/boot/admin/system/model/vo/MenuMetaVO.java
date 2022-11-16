package com.boot.admin.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "元菜单 视图展示对象")
public class MenuMetaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "是否缓存")
    private Integer cache;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getCache() {
        return cache;
    }

    public void setCache(Integer cache) {
        this.cache = cache;
    }

    public MenuMetaVO() {
    }

    public MenuMetaVO(String title, String icon, Integer cache) {
        this.title = title;
        this.icon = icon;
        this.cache = cache;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
