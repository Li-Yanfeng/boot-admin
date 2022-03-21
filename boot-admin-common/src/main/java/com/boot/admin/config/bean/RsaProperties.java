package com.boot.admin.config.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RSA 属性配置
 *
 * @author Li Yanfeng
 */
@Component
public class RsaProperties {

    /**
     * 公钥
     */
    public static String publicKey;
    /**
     * 私钥
     */
    public static String privateKey;


    public static String getPublicKey() {
        return publicKey;
    }

    @Value(value = "${rsa.public-key}")
    public void setPublicKey(String publicKey) {
        RsaProperties.publicKey = publicKey;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    @Value(value = "${rsa.private-key}")
    public void setPrivateKey(String privateKey) {
        RsaProperties.privateKey = privateKey;
    }
}
