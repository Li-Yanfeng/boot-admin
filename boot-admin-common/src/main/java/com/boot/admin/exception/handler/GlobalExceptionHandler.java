package com.boot.admin.exception.handler;

import com.boot.admin.core.model.Result;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.EntityExistException;
import com.boot.admin.exception.EntityNotFoundException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.util.ThrowableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 全局异常处理程序
 *
 * @author Li Yanfeng
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BadRequestException.class)
    public Result handlerBadRequestException(BadRequestException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(e.getCode(), e.getMessage());
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = EntityExistException.class)
    public Result handlerEntityExistException(EntityExistException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(UserErrorCode.DATA_HAS_UNIQUENESS.getCode(), e.getMessage());
    }

    /**
     * 处理 EntityNotFound
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public Result handlerEntityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(UserErrorCode.REQUEST_DATA_NOT_FOUND.getCode(), e.getMessage());
    }

    /**
     * 处理 AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handlerAccessDeniedException(AccessDeniedException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(UserErrorCode.NO_ACCESS_TO_API);
    }

    /**
     * 处理 BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public Result handlerBadCredentialsException(BadCredentialsException e) {
        // 打印堆栈信息
        String message = "坏的凭证".equals(e.getMessage()) ? "用户名或密码不正确" : e.getMessage();
        logger.error(message);
        return Result.failure(message);
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message))
            message = str[1] + ":" + message;
        return Result.failure(message);
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public Result handleException(Throwable e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(UserErrorCode.CLIENT_ERROR);
    }
}
