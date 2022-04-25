package com.boot.admin.exception;

import com.boot.admin.util.StringUtils;

/**
 * 实体存在异常
 *
 * @author liYanfeng
 */
public class EntityExistException extends RuntimeException {

    public EntityExistException(String message) {
        super(StringUtils.format("【%s】已存在", message));
    }
}
