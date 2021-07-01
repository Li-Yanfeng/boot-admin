package org.utility.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.utility.exception.BadRequestException;
import org.utility.mapper.AlipayMapper;
import org.utility.model.AlipayConfig;
import org.utility.model.vo.TradeVO;
import org.utility.service.AlipayService;

/**
 * 支付宝配置 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Service
@CacheConfig(cacheNames = {"aliPay"})
public class AlipayServiceImpl implements AlipayService {

    private final AlipayMapper alipayMapper;

    public AlipayServiceImpl(AlipayMapper alipayMapper) {
        this.alipayMapper = alipayMapper;
    }

    @CachePut(key = "'config'")
    @Override
    public AlipayConfig config(AlipayConfig alipayConfig) {
        alipayConfig.setConfigId(1L);
        alipayMapper.updateById(alipayConfig);
        return alipayConfig;
    }

    @Cacheable(key = "'config'")
    @Override
    public AlipayConfig getConfig() {
        return alipayMapper.selectById(1L);
    }

    @Override
    public String toPayAsPc(AlipayConfig alipay, TradeVO trade) throws Exception {
        if (alipay.getConfigId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(), alipay.getAppId(),
                alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(),
                alipay.getSignType());

        // 创建API对应的request(电脑网页版)
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 订单完成后返回的页面和异步通知地址
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        // 填充订单参数
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + trade.getOutTradeNo() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + trade.getTotalAmount() + "," +
                "    \"subject\":\"" + trade.getSubject() + "\"," +
                "    \"body\":\"" + trade.getBody() + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + alipay.getSysServiceProviderId() + "\"" +
                "    }" +
                "  }");//填充业务参数
        // 调用SDK生成表单, 通过GET方式，口可以获取url
        return alipayClient.pageExecute(request, "GET").getBody();
    }

    @Override
    public String toPayAsWeb(AlipayConfig alipay, TradeVO trade) throws Exception {
        if (alipay.getConfigId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(), alipay.getAppId(),
                alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(),
                alipay.getSignType());

        double money = Double.parseDouble(trade.getTotalAmount());
        double maxMoney = 5000;
        if (money <= 0 || money >= maxMoney) {
            throw new BadRequestException("测试金额过大");
        }
        // 创建API对应的request(手机网页版)
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + trade.getOutTradeNo() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + trade.getTotalAmount() + "," +
                "    \"subject\":\"" + trade.getSubject() + "\"," +
                "    \"body\":\"" + trade.getBody() + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + alipay.getSysServiceProviderId() + "\"" +
                "    }" +
                "  }");
        return alipayClient.pageExecute(request, "GET").getBody();
    }
}
