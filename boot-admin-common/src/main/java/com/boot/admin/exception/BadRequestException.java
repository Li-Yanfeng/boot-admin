package com.boot.admin.exception;

import com.boot.admin.core.interfaces.ErrorCode;
import com.boot.admin.exception.enums.UserErrorCode;

/**
 * 自定义异常
 *
 * @author liYanfeng
 */
public class BadRequestException extends RuntimeException {

    /**
     * 业务错误码
     */
    private String code = UserErrorCode.CLIENT_ERROR.getCode();


    public String getCode() {
        return code;
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getUserTip());
        this.code = errorCode.getCode();
    }
}
