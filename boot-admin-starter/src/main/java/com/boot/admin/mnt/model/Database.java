package com.boot.admin.mnt.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.core.model.BaseEntity;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "数据库")
@TableName(value = "mnt_database")
public class Database extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = ValidGroup.Update.class)
    private Long dbId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "jdbc连接")
    private String jdbcUrl;

    @Schema(description = "账号")
    private String userName;

    @Schema(description = "密码")
    private String pwd;


    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
