package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.google.common.base.Objects;
import org.utility.base.BaseDTO;

import java.io.Serializable;

/**
 * 角色 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class RoleSmallDTO extends BaseDTO implements Serializable {

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
     * 数据权限
     */
    private String dataScope;


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

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleSmallDTO)) return false;
        RoleSmallDTO that = (RoleSmallDTO) o;
        return Objects.equal(roleId, that.roleId) && Objects.equal(name, that.name) && Objects.equal(level,
                that.level) && Objects.equal(dataScope, that.dataScope);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleId, name, level, dataScope);
    }

    @Override
    public String toString() {
        return "RoleSmallDTO{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", dataScope='" + dataScope + '\'' +
                '}';
    }
}
