package org.utility.modules.mnt.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.base.BaseDTO;

import java.io.Serializable;
import java.util.Objects;

/**
 * 服务器管理 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class ServerDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long serverId;
    /**
     * 名称
     */
    private String name;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;


    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerDTO that = (ServerDTO) o;
        return Objects.equals(serverId, that.serverId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, name);
    }

    @Override
    public String toString() {
        return "ServerDTO{" +
                "serverId=" + serverId +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
