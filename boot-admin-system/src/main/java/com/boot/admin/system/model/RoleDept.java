package com.boot.admin.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "角色部门关联")
@TableName(value = "sys_role_dept")
public class RoleDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    @TableField
    private Long roleId;

    @Schema(description = "部门ID")
    @TableField
    private Long deptId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
