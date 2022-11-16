package com.boot.admin.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.core.model.BaseEntity;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "菜单")
@TableName(value = "sys_menu")
public class Menu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long menuId;

    @Schema(description = "上级菜单ID")
    private Long pid;

    @Schema(description = "菜单类型")
    private Integer type;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "组件名称")
    private String componentName;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "权限")
    private String permission;

    @Schema(description = "是否外链")
    @TableField(value = "is_i_frame")
    private Integer iFrame;

    @Schema(description = "链接地址")
    private String path;

    @Schema(description = "是否缓存")
    @TableField(value = "is_cache")
    private Integer cache;

    @Schema(description = "是否隐藏")
    @TableField(value = "is_hidden")
    private Integer hidden;

    @Schema(description = "排序")
    private Integer menuSort;

    @Schema(description = "子菜单数目")
    private Integer subCount;


    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getIFrame() {
        return iFrame;
    }

    public void setIFrame(Integer iFrame) {
        this.iFrame = iFrame;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getCache() {
        return cache;
    }

    public void setCache(Integer cache) {
        this.cache = cache;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public Integer getSubCount() {
        return subCount;
    }

    public void setSubCount(Integer subCount) {
        this.subCount = subCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Menu menu = (Menu) o;
        return Objects.equals(menuId, menu.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }
}
