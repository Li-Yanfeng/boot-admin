package com.boot.admin.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.core.model.BaseEntity;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "用户")
@TableName(value = "sys_user")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long userId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像名称")
    private String avatarName;

    @Schema(description = "头像真实路径")
    private String avatarPath;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "是否为admin账号")
    @TableField(value = "is_admin")
    private Integer admin;

    @Schema(description = "是否启用")
    @TableField(value = "is_enabled")
    private Integer enabled;

    @Schema(description = "修改密码时间")
    private LocalDateTime pwdResetTime;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getPwdResetTime() {
        return pwdResetTime;
    }

    public void setPwdResetTime(LocalDateTime  pwdResetTime) {
        this.pwdResetTime = pwdResetTime;
    }
}
