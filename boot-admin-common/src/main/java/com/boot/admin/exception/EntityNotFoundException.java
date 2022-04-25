package com.boot.admin.exception;

/**
 * 实体找不到异常
 *
 * @author liYanfeng
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
