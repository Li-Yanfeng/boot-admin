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
@Schema(description = "角色 数据传输对象")
public class RoleDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long roleId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "角色级别")
    private Integer level;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "数据权限")
    private String dataScope;

    @Schema(description = "菜单")
    private List<MenuDTO> menus;

    @Schema(description = "部门")
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
