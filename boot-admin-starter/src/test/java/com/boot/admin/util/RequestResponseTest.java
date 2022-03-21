package com.boot.admin.util;

import com.boot.admin.config.bean.RsaProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 请求体/响应体 测试类
 *
 * @author Li Yanfeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestResponseTest {

    /**
     * 使用公钥对请求体加密 测试
     */
    @Test
    public void testPublicKeyEncryptRequestBody() throws Exception {
        String requestBody = "{\"pid\": 0, \"name\": \"财务部\", \"enabled\": 1, \"dept_sort\": 3}";
        String encryptBody = RsaUtils.encryptByPublicKey(RsaProperties.publicKey, requestBody);
        System.out.println("EncryptBody: " + encryptBody);
    }


    /**
     * 使用私钥对请求体解密 测试
     */
    @Test
    public void testPrivateKeyDecryptRequestBody() throws Exception {
        String requestBody = "X2PmU5T/h55zN21LdGh/mPyti07weLCmkmGwBC7CNLAWs8emgiTmxu21k7qfi/MU88U3D9jlRJ+QTvA+rpE1cUTazaVNBLtFV4NgbR76l2ErLmZaIGpWkNX+kY1uUq3Cnd3e9RRPecAeAyrF/9MUtdhz3RQhXa8nky9gdfivbOg=";
        String decryptBody = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, requestBody);
        System.out.println("EncryptBody: " + decryptBody);
    }
}
