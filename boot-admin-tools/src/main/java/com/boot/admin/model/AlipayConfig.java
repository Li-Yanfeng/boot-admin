package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "支付宝配置")
@TableName(value = "tool_alipay_config")
public class AlipayConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long configId;

    @Schema(description = "应用ID")
    @NotBlank
    private String appId;

    @Schema(description = "商户号")
    @NotBlank
    private String sysServiceProviderId;

    @Schema(description = "商户私钥")
    @NotBlank
    private String privateKey;

    @Schema(description = "支付宝公钥")
    @NotBlank
    private String publicKey;

    @Schema(description = "签名方式")
    private String signType = "RSA2";

    @Schema(description = "编码", hidden = true)
    private String charset = "utf-8";

    @Schema(description = "类型")
    private String format = "JSON";

    @Schema(description = "支付宝开放安全地址", hidden = true)
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    @Schema(description = "异步通知地址")
    private String notifyUrl;

    @NotBlank
    @Schema(description = "订单完成后返回的页面")
    private String returnUrl;


    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSysServiceProviderId() {
        return sysServiceProviderId;
    }

    public void setSysServiceProviderId(String sysServiceProviderId) {
        this.sysServiceProviderId = sysServiceProviderId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
