package org.utility.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RSA 属性配置
 *
 * @author Li Yanfeng
 */
@Component
public class RsaProperties {

    @Value("${rsa.private-key}")
    public static String privateKey;


    public void setPrivateKey(String privateKey) {
        RsaProperties.privateKey = privateKey;
    }

    public static String getPrivateKey() {
        return privateKey;
    }
}
