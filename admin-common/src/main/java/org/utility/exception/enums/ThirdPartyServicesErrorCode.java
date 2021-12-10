package org.utility.exception.enums;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.utility.core.interfaces.ErrorCode;

/**
 * 第三方服务错误代码 { C 表示错误来源于第三方服务 }
 * 1. 以下错误码的定义，需要提前与前端沟通
 * 2. 错误码按模块进行错误码规划
 * 3. 所有错误码枚举类均需要实现错误码接口类
 *
 * @author Li Yanfeng
 * @see UserErrorCode 错误来源于用户
 * @see SystemErrorCode 错误来源于当前系统
 * @see ThirdPartyServicesErrorCode 错误来源于第三方服务
 */
public enum ThirdPartyServicesErrorCode implements ErrorCode {

    // C 表示错误来源于第三方服务
    ERROR_IN_CALLING_THIRD_PARTY_SERVICE("C0001", "调用第三方服务出错"),
    MIDDLEWARE_SERVICE_ERROR("C0100", "中间件服务出错"),
    RPC_SERVICE_ERROR("C0110", "RPC 服务出错"),
    RPC_SERVICE_NOT_FOUND("C0111", "RPC 服务未找到"),
    RPC_SERVICE_IS_NOT_REGISTERED("C0112", "RPC 服务未注册"),
    INTERFACE_DOES_NOT_EXIST("C0113", "接口不存在"),
    MESSAGE_SERVICE_ERROR("C0120", "消息服务出错"),
    MESSAGE_DELIVERY_ERROR("C0121", "消息投递出错"),
    MESSAGE_CONSUMPTION_ERROR("C0122", "消息消费出错"),
    MESSAGE_SUBSCRIPTION_ERROR("C0123", "消息订阅出错"),
    MESSAGE_GROUP_NOT_FOUND("C0124", "消息分组未查到"),
    CACHE_SERVICE_ERROR("C0130", "缓存服务出错"),
    THE_KEY_LENGTH_EXCEEDS_THE_LIMIT("C0131", "key 长度超过限制"),
    VALUE_LENGTH_EXCEEDS_THE_LIMIT("C0132", "value 长度超过限制"),
    STORAGE_CAPACITY_IS_FULL("C0133", "存储容量已满"),
    UNSUPPORTED_DATA_FORMAT("C0134", "不支持的数据格式"),
    ERROR_CONFIGURING_SERVICE("C0140", "配置服务出错"),
    NETWORK_RESOURCE_SERVICE_ERROR("C0150", "网络资源服务出错"),
    VPN_SERVICE_ERROR("C0151", "VPN 服务出错"),
    CDN_SERVICE_ERROR("C0152", "CDN 服务出错"),
    DOMAIN_NAME_RESOLUTION_SERVICE_ERROR("C0153", "域名解析服务出错"),
    GATEWAY_SERVICE_ERROR("C0154", "网关服务出错"),
    THIRD_PARTY_SYSTEM_EXECUTION_TIMEOUT("C0200", "第三方系统执行超时"),
    RPC_EXECUTION_TIMEOUT("C0210", "RPC 执行超时"),
    MESSAGE_DELIVERY_TIMEOUT("C0220", "消息投递超时"),
    CACHE_SERVICE_TIMEOUT("C0230", "缓存服务超时"),
    CONFIGURE_SERVICE_TIMEOUT("C0240", "配置服务超时"),
    DATABASE_SERVICE_TIMEOUT("C0250", "数据库服务超时"),
    DATABASE_SERVICE_ERROR("C0300", "数据库服务出错"),
    TABLE_DOES_NOT_EXIST("C0311", "表不存在"),
    COLUMN_DOES_NOT_EXIST("C0312", "列不存在"),
    THERE_ARE_MULTIPLE_COLUMNS_WITH_THE_SAME_NAME_IN_A_MULTI_TABLE_ASSOCIATION("C0321", "多表关联中存在多个相同名称的列"),
    DATABASE_DEADLOCK("C0331", "数据库死锁"),
    PRIMARY_KEY_CONFLICT("C0341", "主键冲突"),
    THE_THIRD_PARTY_DISASTER_RECOVERY_SYSTEM_IS_TRIGGERED("C0400", "第三方容灾系统被触发"),
    THIRD_PARTY_SYSTEM_CURRENT_LIMIT("C0401", "第三方系统限流"),
    DEGRADATION_OF_THIRD_PARTY_FUNCTIONS("C0402", "第三方功能降级"),
    NOTIFICATION_SERVICE_ERROR("C0500", "通知服务出错"),
    SMS_REMINDER_SERVICE_FAILED("C0501", "短信提醒服务失败"),
    VOICE_REMINDER_SERVICE_FAILED("C0502", "语音提醒服务失败"),
    EMAIL_REMINDER_SERVICE_FAILED("C0503", "邮件提醒服务失败");


    /**
     * 业务状态码
     */
    private final String code;
    /**
     * 用户提示
     */
    private final String userTip;


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getUserTip() {
        return userTip;
    }

    ThirdPartyServicesErrorCode(final String code, final String userTip) {
        this.code = code;
        this.userTip = userTip;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
