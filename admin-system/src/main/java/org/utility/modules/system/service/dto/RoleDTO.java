package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.google.common.base.Objects;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;
import java.util.Set;

/**
 * 角色 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class RoleDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long roleId;
    /**
     * 名称
     */
    private String name;
    /**
     * 角色级别
     */
    private Integer level;
    /**
     * 描述
     */
    private String description;
    /**
     * 数据权限
     */
    private String dataScope;

    /**
     * 菜单
     */
    private Set<MenuDTO> menus;
    /**
     * 部门
     */
    private Set<DeptDTO> depts;


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

    public Set<MenuDTO> getMenus() {
        return menus;
    }

    public void setMenus(Set<MenuDTO> menus) {
        this.menus = menus;
    }

    public Set<DeptDTO> getDepts() {
        return depts;
    }

    public void setDepts(Set<DeptDTO> depts) {
        this.depts = depts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleDTO)) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equal(roleId, roleDTO.roleId) && Objects.equal(name, roleDTO.name) && Objects.equal(level,
                roleDTO.level) && Objects.equal(description, roleDTO.description) && Objects.equal(dataScope,
                roleDTO.dataScope) && Objects.equal(menus, roleDTO.menus) && Objects.equal(depts, roleDTO.depts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleId, name, level, description, dataScope, menus, depts);
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", description='" + description + '\'' +
                ", dataScope='" + dataScope + '\'' +
                ", menus=" + menus +
                ", depts=" + depts +
                '}';
    }
}
