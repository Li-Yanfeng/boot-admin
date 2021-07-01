package org.utility.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 七牛云存储 配置类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@TableName(value = "tool_qiniu_config")
public class QiniuConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private Long configId;

    @ApiModelProperty(value = "accessKey")
    @NotBlank
    private String accessKey;

    @ApiModelProperty(value = "secretKey")
    @NotBlank
    private String secretKey;

    @ApiModelProperty(value = "存储空间名称作为唯一的 Bucket 识别符")
    @NotBlank
    private String bucket;

    /**
     * Zone表示与机房的对应关系
     * 华东	Zone.zone0()
     * 华北	Zone.zone1()
     * 华南	Zone.zone2()
     * 北美	Zone.zoneNa0()
     * 东南亚	Zone.zoneAs0()
     */
    @ApiModelProperty(value = "Zone表示与机房的对应关系")
    @NotBlank
    private String zone;

    @ApiModelProperty(value = "外链域名，可自定义，需在七牛云绑定")
    @NotBlank
    private String host;

    @ApiModelProperty(value = "空间类型：公开/私有")
    private String type = "公开";


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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
