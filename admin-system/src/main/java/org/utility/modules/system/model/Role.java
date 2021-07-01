package org.utility.modules.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Objects;
import io.swagger.annotations.ApiModelProperty;
import org.utility.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@TableName(value = "sys_role")
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private Long roleId;

    @ApiModelProperty(value = "名称")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "角色级别")
    private Integer level;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "数据权限")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equal(roleId, role.roleId) && Objects.equal(name, role.name) && Objects.equal(level,
                role.level) && Objects.equal(dataScope, role.dataScope);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleId, name, level, dataScope);
    }
}
