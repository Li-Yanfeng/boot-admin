package com.boot.admin.system.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "菜单 数据传输对象")
public class MenuDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
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
    private Integer iFrame;

    @Schema(description = "链接地址")
    private String path;

    @Schema(description = "是否缓存")
    private Integer cache;

    @Schema(description = "是否隐藏")
    private Integer hidden;

    @Schema(description = "排序")
    private Integer menuSort;

    @Schema(description = "子菜单数目")
    private Integer subCount;


    @Schema(description = "子节点")
    private List<MenuDTO> children;

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

    public List<MenuDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDTO> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuDTO menuDTO = (MenuDTO) o;
        return Objects.equals(menuId, menuDTO.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
