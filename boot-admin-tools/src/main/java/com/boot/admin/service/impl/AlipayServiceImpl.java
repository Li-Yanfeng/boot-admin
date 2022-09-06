package com.boot.admin.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mapper.AlipayConfigMapper;
import com.boot.admin.model.AlipayConfig;
import com.boot.admin.model.vo.TradeVO;
import com.boot.admin.service.AlipayService;
import com.boot.admin.util.RedisUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 支付宝配置 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"alipay"})
public class AlipayServiceImpl extends ServiceImpl<AlipayConfigMapper, AlipayConfig> implements AlipayService {

    private final RedisUtils redisUtils;

    public AlipayServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void updateAlipayConfig(AlipayConfig resource) {
        resource.setConfigId(1L);
        baseMapper.updateById(resource);
        // 更新缓存
        redisUtils.set(CacheKey.ALIPAY_CONFIG, resource);
    }

    @Cacheable(key = "'config'")
    @Override
    public AlipayConfig getAlipayConfig() {
        return baseMapper.selectById(1L);
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
