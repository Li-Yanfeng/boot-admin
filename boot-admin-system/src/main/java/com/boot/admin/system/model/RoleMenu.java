package com.boot.admin.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "角色菜单关联")
@TableName(value = "sys_role_menu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    @TableField
    private Long menuId;

    @Schema(description = "角色ID")
    @TableField
    private Long roleId;


    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
