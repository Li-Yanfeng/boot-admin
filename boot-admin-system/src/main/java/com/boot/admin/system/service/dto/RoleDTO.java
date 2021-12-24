package com.boot.admin.system.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "角色 数据传输对象")
public class RoleDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long roleId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "角色级别")
    private Integer level;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "数据权限")
    private String dataScope;

    @ApiModelProperty(value = "菜单")
    private List<MenuDTO> menus;

    @ApiModelProperty(value = "部门")
    private List<DeptDTO> depts;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public List<MenuDTO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDTO> menus) {
        this.menus = menus;
    }

    public List<DeptDTO> getDepts() {
        return depts;
    }

    public void setDepts(List<DeptDTO> depts) {
        this.depts = depts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(roleId, roleDTO.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
