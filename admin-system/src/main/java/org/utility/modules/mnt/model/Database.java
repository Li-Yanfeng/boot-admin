package org.utility.modules.mnt.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import org.utility.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 数据库管理
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@TableName(value = "mnt_database")
public class Database extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "db_id", type = IdType.ASSIGN_ID)
    private Long dbId;

    @ApiModelProperty(value = "名称")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "jdbc连接")
    @NotBlank
    private String jdbcUrl;

    @ApiModelProperty(value = "账号")
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "密码")
    @NotBlank
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
