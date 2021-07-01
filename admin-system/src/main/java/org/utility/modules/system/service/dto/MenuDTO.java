package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.google.common.base.Objects;
import org.utility.base.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class MenuDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long menuId;
    /**
     * 上级菜单ID
     */
    private Long pid;
    /**
     * 子菜单数目
     */
    private Integer subCount;
    /**
     * 菜单类型
     */
    private Integer type;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 组件名称
     */
    private String componentName;
    /**
     * 组件
     */
    private String component;
    /**
     * 排序
     */
    private Integer menuSort;
    /**
     * 图标
     */
    private String icon;
    /**
     * 链接地址
     */
    private String path;
    /**
     * 是否外链
     */
    private Boolean iFrame;
    /**
     * 缓存
     */
    private Boolean cache;
    /**
     * 隐藏
     */
    private Boolean hidden;
    /**
     * 权限
     */
    private String permission;

    /**
     * 子菜单
     */
    private List<MenuDTO> children;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return title;
    }


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

    public Integer getSubCount() {
        return subCount;
    }

    public void setSubCount(Integer subCount) {
        this.subCount = subCount;
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

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getiFrame() {
        return iFrame;
    }

    public void setiFrame(Boolean iFrame) {
        this.iFrame = iFrame;
    }

    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<MenuDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDTO> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuDTO)) return false;
        MenuDTO menuDTO = (MenuDTO) o;
        return Objects.equal(menuId, menuDTO.menuId) && Objects.equal(pid, menuDTO.pid) && Objects.equal(subCount, menuDTO.subCount) && Objects.equal(type, menuDTO.type) && Objects.equal(title, menuDTO.title) && Objects.equal(componentName, menuDTO.componentName) && Objects.equal(component, menuDTO.component) && Objects.equal(menuSort, menuDTO.menuSort) && Objects.equal(icon, menuDTO.icon) && Objects.equal(path, menuDTO.path) && Objects.equal(iFrame, menuDTO.iFrame) && Objects.equal(cache, menuDTO.cache) && Objects.equal(hidden, menuDTO.hidden) && Objects.equal(permission, menuDTO.permission) && Objects.equal(children, menuDTO.children);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(menuId, pid, subCount, type, title, componentName, component, menuSort, icon, path, iFrame, cache, hidden, permission, children);
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
                "menuId=" + menuId +
                ", pid=" + pid +
                ", subCount=" + subCount +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", componentName='" + componentName + '\'' +
                ", component='" + component + '\'' +
                ", menuSort=" + menuSort +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                ", iFrame=" + iFrame +
                ", cache=" + cache +
                ", hidden=" + hidden +
                ", permission='" + permission + '\'' +
                ", children=" + children +
                '}';
    }
}
