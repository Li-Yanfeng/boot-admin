package com.boot.admin.rest;

import com.boot.admin.annotation.AnonymousAccess;
import com.boot.admin.annotation.Log;
import com.boot.admin.annotation.NoRepeatSubmit;
import com.boot.admin.annotation.ResultWrapper;
import com.boot.admin.annotation.rest.AnonymousGetMapping;
import com.boot.admin.core.model.Result;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.model.AlipayConfig;
import com.boot.admin.model.vo.TradeVO;
import com.boot.admin.service.AlipayService;
import com.boot.admin.util.AliPayStatusEnum;
import com.boot.admin.util.AlipayUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Tag(name = "工具：支付宝管理")
@RestController
@RequestMapping(value = "/api/ali_pays")
@ResultWrapper
public class AlipayController {

    private final AlipayService alipayService;

    private final AlipayUtils alipayUtils;

    public AlipayController(AlipayService alipayService, AlipayUtils alipayUtils) {
        this.alipayService = alipayService;
        this.alipayUtils = alipayUtils;
    }

    @Operation(summary = "修改支付宝配置")
    @Log(value = "修改支付宝配置")
    @NoRepeatSubmit
    @PutMapping
    public void update(@Validated @RequestBody AlipayConfig resource) {
        alipayService.updateAlipayConfig(resource);
    }

    @Operation(summary = "获取支付宝配置")
    @GetMapping
    public AlipayConfig config() {
        return alipayService.getAlipayConfig();
    }

    @Operation(summary = "PC 网页支付")
    @Log(value = "支付宝PC网页支付")
    @NoRepeatSubmit
    @PostMapping(value = "/pcs")
    public String toPayAsPc(@Validated @RequestBody TradeVO trade) throws Exception {
        AlipayConfig aliPay = alipayService.getAlipayConfig();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        return alipayService.toPayAsPc(aliPay, trade);
    }

    @Operation(summary = "手机网页支付")
    @Log(value = "支付宝手机网页支付")
    @NoRepeatSubmit
    @PostMapping(value = "/webs")
    public String toPayAsWeb(@Validated @RequestBody TradeVO trade) throws Exception {
        AlipayConfig alipay = alipayService.getAlipayConfig();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        return alipayService.toPayAsWeb(alipay, trade);
    }

    @Operation(summary = "支付之后跳转的链接", hidden = true)
    @AnonymousGetMapping(value = "/return")
    public Result returnPage(HttpServletRequest request, HttpServletResponse response) {
        AlipayConfig alipay = alipayService.getAlipayConfig();
        response.setContentType("text/html;charset=" + alipay.getCharset());
        // 内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1)
                , StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
            System.out.println("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

            // 根据业务需要返回数据，这里统一返回OK
            return Result.success("payment successful");
        } else {
            // 根据业务需要返回数据
            return Result.success(UserErrorCode.CLIENT_ERROR);
        }
    }

    @Operation(summary = "支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理", hidden = true)
    @AnonymousAccess
    @AnonymousGetMapping(value = "/notifies")
    public Result notify(HttpServletRequest request) {
        AlipayConfig alipay = alipayService.getAlipayConfig();
        // 内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            // 交易状态
            String tradeStatus =
                new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8);
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1)
                , StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
            // 付款金额
            String totalAmount =
                new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8);
            // 验证
            if (tradeStatus.equals(AliPayStatusEnum.SUCCESS.getValue()) || tradeStatus.equals(AliPayStatusEnum.FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
            }
            return Result.success();
        }
        return Result.success(UserErrorCode.CLIENT_ERROR);
    }
}
