package org.utility.model.vo;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 易详情，按需应该存入数据库，这里存入数据库，仅供临时测试
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
public class TradeVO {

    /**
     * 商品描述（必填）
     */
    @NotBlank
    private String body;
    /**
     * 商品名称（必填）
     */
    @NotBlank
    private String subject;
    /**
     * 商户订单号，应该由后台生成（必填）
     */
    private String outTradeNo;
    /**
     * 第三方订单号（必填）
     */
    private String tradeNo;
    /**
     * 价格（必填）
     */
    @NotBlank
    private String totalAmount;
    /**
     * 订单状态,已支付，未支付，作废
     */
    private String state;
    /**
     * 创建时间，存入数据库时需要
     */
    private Timestamp createTime;
    /**
     * 作废时间，存入数据库时需要
     */
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
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
        return "TradeVO{" +
                "body='" + body + '\'' +
                ", subject='" + subject + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", state='" + state + '\'' +
                ", createTime=" + createTime +
                ", cancelTime=" + cancelTime +
                '}';
    }
}
