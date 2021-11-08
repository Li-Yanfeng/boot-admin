package org.utility.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.utility.api.Result;
import org.utility.exception.BadRequestException;
import org.utility.exception.EntityExistException;
import org.utility.exception.EntityNotFoundException;
import org.utility.exception.enums.UserErrorCode;
import org.utility.util.ThrowableUtils;

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
    public Result<Object> handlerBadRequestException(BadRequestException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(e.getCode(), e.getMessage());
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = EntityExistException.class)
    public Result<Object> handlerEntityExistException(EntityExistException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(e.getMessage());
    }

    /**
     * 处理 EntityNotFound
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public Result<Object> handlerEntityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(UserErrorCode.ENTITY_NOT_FOUND.getCode(), e.getMessage());
    }

    /**
     * 处理 AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> handlerAccessDeniedException(AccessDeniedException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(UserErrorCode.NO_ACCESS_TO_API);
    }

    /**
     * 处理 BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public Result<Object> handlerBadCredentialsException(BadCredentialsException e) {
        // 打印堆栈信息
        String message = "坏的凭证".equals(e.getMessage()) ? "用户名或密码不正确" : e.getMessage();
        logger.error(message);
        return Result.failure(message);
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) message = str[1] + ":" + message;
        return Result.failure(message);
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public Result<Object> handleException(Throwable e) {
        // 打印堆栈信息
        logger.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(UserErrorCode.CLIENT_ERROR);
    }
}
