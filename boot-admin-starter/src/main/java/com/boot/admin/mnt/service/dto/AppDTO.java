package com.boot.admin.mnt.service.dto;

import com.boot.admin.core.service.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "应用 数据传输对象")
public class AppDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long appId;

    @Schema(description = "应用名称")
    private String name;

    @Schema(description = "上传目录")
    private String uploadPath;

    @Schema(description = "部署路径")
    private String deployPath;

    @Schema(description = "备份路径")
    private String backupPath;

    @Schema(description = "应用端口")
    private Integer port;

    @Schema(description = "启动脚本")
    private String startScript;

    @Schema(description = "部署脚本")
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
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
