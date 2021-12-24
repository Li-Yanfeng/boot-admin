package com.boot.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@ApiModel(description = "交易详情，按需存入数据库")
public class TradeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品描述")
    @NotBlank
    private String body;

    @ApiModelProperty(value = "商品名称")
    @NotBlank
    private String subject;

    @ApiModelProperty(value = "商户订单号，应该由后台生成", hidden = true)
    private String outTradeNo;

    @ApiModelProperty(value = "第三方订单号", hidden = true)
    private String tradeNo;

    @ApiModelProperty(value = "价格")
    @NotBlank
    private String totalAmount;

    @ApiModelProperty(value = "订单状态（已支付，未支付，作废）", hidden = true)
    private String state;

    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "作废时间", hidden = true)
    private Date cancelTime;


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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
