package org.utility.modules.mnt.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.utility.core.service.dto.BaseDTO;

import java.io.Serializable;

/**
 * 应用管理 数据传输对象
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
public class AppDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long appId;
    /**
     * 应用名称
     */
    private String name;
    /**
     * 上传目录
     */
    private String uploadPath;
    /**
     * 部署路径
     */
    private String deployPath;
    /**
     * 备份路径
     */
    private String backupPath;
    /**
     * 应用端口
     */
    private Integer port;
    /**
     * 启动脚本
     */
    private String startScript;
    /**
     * 部署脚本
     */
    private String deployScript;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getDeployPath() {
        return deployPath;
    }

    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStartScript() {
        return startScript;
    }

    public void setStartScript(String startScript) {
        this.startScript = startScript;
    }

    public String getDeployScript() {
        return deployScript;
    }

    public void setDeployScript(String deployScript) {
        this.deployScript = deployScript;
    }

    @Override
    public String toString() {
        return "AppDTO{" +
                "Long='" + appId + '\'' +
                ", String='" + name + '\'' +
                ", String='" + uploadPath + '\'' +
                ", String='" + deployPath + '\'' +
                ", String='" + backupPath + '\'' +
                ", Integer='" + port + '\'' +
                ", String='" + startScript + '\'' +
                ", String='" + deployScript + '\'' +
                '}';
    }
}
