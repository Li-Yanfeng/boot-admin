package org.utility.service;

import org.utility.core.service.Service;
import org.utility.model.AlipayConfig;
import org.utility.model.vo.TradeVO;

/**
 * 支付宝配置 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface AlipayService extends Service<AlipayConfig> {

    /**
     * 更新 支付宝配置
     *
     * @param resource 支付宝配置
     */
    void updateAlipayConfig(AlipayConfig resource);

    /**
     * 查询 支付宝配置
     *
     * @return AlipayConfig
     */
    AlipayConfig getAlipayConfig();

    /**
     * 处理来自 PC 的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade  交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsPc(AlipayConfig alipay, TradeVO trade) throws Exception;

    /**
     * 处理来自 手机网页 的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade  交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsWeb(AlipayConfig alipay, TradeVO trade) throws Exception;
}
