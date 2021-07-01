package org.utility.modules.mnt.service.dto;

import org.utility.base.BaseDTO;

import java.io.Serializable;

/**
 * 数据库管理 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class DatabaseDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long dbId;
    /**
     * 名称
     */
    private String name;
    /**
     * jdbc连接
     */
    private String jdbcUrl;
    /**
     * 账号
     */
    private String userName;
    /**
     * 密码
     */
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

    @Override
    public String toString() {
        return "DatabaseDTO{" +
                "String='" + dbId + '\'' +
                ", String='" + name + '\'' +
                ", String='" + jdbcUrl + '\'' +
                ", String='" + userName + '\'' +
                ", String='" + pwd + '\'' +
                '}';
    }
}
