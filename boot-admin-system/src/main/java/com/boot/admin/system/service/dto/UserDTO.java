package com.boot.admin.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.boot.admin.core.service.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "用户 数据传输对象")
public class UserDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
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
    @JSONField(serialize = false)
    private String password;

    @Schema(description = "是否为admin账号")
    @JSONField(serialize = false)
    private Integer admin;

    @Schema(description = "是否启用")
    private Integer enabled;

    @Schema(description = "修改密码时间")
    private LocalDateTime pwdResetTime;


    @Schema(description = "角色")
    private List<RoleSmallDTO> roles;

    @Schema(description = "岗位")
    private List<JobSmallDTO> jobs;

    @Schema(description = "部门")
    private DeptSmallDTO dept;


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

    public List<RoleSmallDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleSmallDTO> roles) {
        this.roles = roles;
    }

    public List<JobSmallDTO> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobSmallDTO> jobs) {
        this.jobs = jobs;
    }

    public DeptSmallDTO getDept() {
        return dept;
    }

    public void setDept(DeptSmallDTO dept) {
        this.dept = dept;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(userId, userDTO.userId) && Objects.equals(username, userDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
