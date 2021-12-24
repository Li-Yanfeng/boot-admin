package com.boot.admin.core.interfaces;

import com.boot.admin.exception.enums.SystemErrorCode;
import com.boot.admin.exception.enums.ThirdPartyServicesErrorCode;
import com.boot.admin.exception.enums.UserErrorCode;

/**
 * REST API 错误码接口
 * 1. 以下错误码的定义，需要提前与前端沟通
 * 2. 错误码按模块进行错误码规划
 * 3. 所有错误码枚举类均需要实现错误码接口类
 *
 * @author Li Yanfeng
 * @see UserErrorCode 错误来源于用户
 * @see SystemErrorCode 错误来源于当前系统
 * @see ThirdPartyServicesErrorCode 错误来源于第三方服务
 */
public interface ErrorCode {

    /**
     * 获取业务错误码
     *
     * @return /
     */
    String getCode();

    /**
     * 获取用户提示
     *
     * @return /
     */
    String getUserTip();
}
