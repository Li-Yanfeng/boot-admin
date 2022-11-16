package com.boot.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "交易详情，按需存入数据库")
public class TradeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商品描述")
    @NotBlank
    private String body;

    @Schema(description = "商品名称")
    @NotBlank
    private String subject;

    @Schema(description = "商户订单号，应该由后台生成", hidden = true)
    private String outTradeNo;

    @Schema(description = "第三方订单号", hidden = true)
    private String tradeNo;

    @Schema(description = "价格")
    @NotBlank
    private String totalAmount;

    @Schema(description = "订单状态（已支付，未支付，作废）", hidden = true)
    private String state;

    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;

    @Schema(description = "作废时间", hidden = true)
    private LocalDateTime cancelTime;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime  createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(LocalDateTime  cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
