package org.utility.exception;

import org.utility.core.interfaces.ErrorCode;
import org.utility.exception.enums.UserErrorCode;

/**
 * 自定义异常
 *
 * @author LiYanfeng
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
