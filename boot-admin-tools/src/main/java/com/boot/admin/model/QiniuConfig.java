package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "七牛云存储配置")
@TableName(value = "tool_qiniu_config")
public class QiniuConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(groups = ValidGroup.Update.class)
    private Long configId;

    @Schema(description = "accessKey")
    @NotBlank
    private String accessKey;

    @Schema(description = "secretKey")
    @NotBlank
    private String secretKey;

    @Schema(description = "存储空间名称作为唯一的 Bucket 识别符")
    @NotBlank
    private String bucket;

    /**
     * Zone表示与机房的对应关系
     * 华东	 Zone.zone0()
     * 华北	 Zone.zone1()
     * 华南	 Zone.zone2()
     * 北美	 Zone.zoneNa0()
     * 东南亚 Zone.zoneAs0()
     */
    @Schema(description = "Zone表示与机房的对应关系")
    @NotBlank
    private String zone;

    @Schema(description = "域名，需在七牛云绑定")
    @NotBlank
    private String domain;

    @Schema(description = "空间类型：私有或公开")
    private String spaceType;


    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(String spaceType) {
        this.spaceType = spaceType;
    }
}
