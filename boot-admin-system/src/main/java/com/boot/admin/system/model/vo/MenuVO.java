package com.boot.admin.system.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "元菜单 视图展示对象")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "组件名称")
    private String name;

    @ApiModelProperty(value = "链接地址")
    private String path;

    @ApiModelProperty(value = "是否隐藏")
    @TableField(value = "is_hidden")
    private Integer hidden;

    @ApiModelProperty(value = "重定向")
    private String redirect;

    @ApiModelProperty(value = "总是显示")
    private Boolean alwaysShow;

    @ApiModelProperty(value = "元菜单")
    private MenuMetaVO meta;

    @ApiModelProperty(value = "子节点")
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
