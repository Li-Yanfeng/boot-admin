package com.boot.admin.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.boot.admin.model.AlipayConfig;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝工具类
 *
 * @author Li Yanfeng
 */
@Component
public class AlipayUtils {

    /**
     * 生成订单号
     *
     * @return String
     */
    public String getOrderCode() {
        int a = (int) (Math.random() * 9000.0D) + 1000;
        String str = DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN);
        String[] split = str.split("-");
        String s = split[0] + split[1] + split[2];
        String[] split1 = s.split(" ");
        String s1 = split1[0] + split1[1];
        String[] split2 = s1.split(":");
        return split2[0] + split2[1] + split2[2] + a;
    }

    /**
     * 校验签名
     *
     * @param request HttpServletRequest
     * @param alipay  阿里云配置
     * @return boolean
     */
    public boolean rsaCheck(HttpServletRequest request, AlipayConfig alipay) {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(1);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                    : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        try {
            return AlipaySignature.rsaCheckV1(params,
                alipay.getPublicKey(),
                alipay.getCharset(),
                alipay.getSignType());
        } catch (AlipayApiException e) {
            return false;
        }
    }
}
