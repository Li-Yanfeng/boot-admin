package org.utility.service;

import org.utility.model.AlipayConfig;
import org.utility.model.vo.TradeVO;

/**
 * 支付宝配置 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public interface AlipayService {

    /**
     * 更新配置
     *
     * @param alipayConfig 支付宝配置
     * @return AlipayConfig
     */
    AlipayConfig config(AlipayConfig alipayConfig);

    /**
     * 查询配置
     *
     * @return AlipayConfig
     */
    AlipayConfig getConfig();

    /**
     * 处理来自PC的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade  交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsPc(AlipayConfig alipay, TradeVO trade) throws Exception;

    /**
     * 处理来自手机网页的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade  交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsWeb(AlipayConfig alipay, TradeVO trade) throws Exception;
}
