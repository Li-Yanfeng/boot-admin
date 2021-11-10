package org.utility.modules.system.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 用户 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class UserDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId;
    /**
     * 部门名称
     */
    private Long deptId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像地址
     */
    private String avatarName;
    /**
     * 头像真实路径
     */
    private String avatarPath;
    /**
     * 密码
     */
    @JSONField(serialize = false)
    private String password;
    /**
     * 是否为admin账号
     */
    @JSONField(serialize = false)
    private Boolean admin;
    /**
     * 状态：1启用、0禁用
     */
    private Boolean enabled;
    /**
     * 修改密码的时间
     */
    private Date pwdResetTime;

    /**
     * 角色
     */
    private Set<RoleSmallDTO> roles;
    /**
     * 岗位
     */
    private Set<JobSmallDTO> jobs;
    /**
     * 部门
     */
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getPwdResetTime() {
        return pwdResetTime;
    }

    public void setPwdResetTime(Date pwdResetTime) {
        this.pwdResetTime = pwdResetTime;
    }

    public Set<RoleSmallDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleSmallDTO> roles) {
        this.roles = roles;
    }

    public Set<JobSmallDTO> getJobs() {
        return jobs;
    }

    public void setJobs(Set<JobSmallDTO> jobs) {
        this.jobs = jobs;
    }

    public DeptSmallDTO getDept() {
        return dept;
    }

    public void setDept(DeptSmallDTO dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", deptId=" + deptId +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", avatarName='" + avatarName + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + admin +
                ", enabled=" + enabled +
                ", pwdResetTime=" + pwdResetTime +
                ", roles=" + roles +
                ", jobs=" + jobs +
                ", dept=" + dept +
                '}';
    }
}
