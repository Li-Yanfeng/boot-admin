package com.boot.admin.system.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "元菜单 视图展示对象")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "组件名称")
    private String name;

    @Schema(description = "链接地址")
    private String path;

    @Schema(description = "是否隐藏")
    @TableField(value = "is_hidden")
    private Integer hidden;

    @Schema(description = "重定向")
    private String redirect;

    @Schema(description = "总是显示")
    private Boolean alwaysShow;

    @Schema(description = "元菜单")
    private MenuMetaVO meta;

    @Schema(description = "子节点")
    private List<MenuVO> children;


    public String getName() {
        return name;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Boolean getAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public MenuMetaVO getMeta() {
        return meta;
    }

    public void setMeta(MenuMetaVO meta) {
        this.meta = meta;
    }

    public List<MenuVO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
