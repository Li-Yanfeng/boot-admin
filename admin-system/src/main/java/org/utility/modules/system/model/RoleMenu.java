package org.utility.modules.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 角色菜单关联
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
@TableName(value = "sys_role_menu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单ID")
    @TableField(value = "menu_id")
    private Long menuId;

    @ApiModelProperty(value = "角色ID")
    @TableField(value = "role_id")
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
