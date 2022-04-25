package com.boot.admin.util;

import com.boot.admin.exception.EntityNotFoundException;
import com.boot.admin.exception.enums.UserErrorCode;

/**
 * 断言某些对象或值是否符合规定，否则抛出异常。
 *
 * @author Li Yanfeng
 */
public class Assert {

    /**
     * 实体找不到
     */
    public static void notNull(Object object) {
        notNull(object, UserErrorCode.REQUEST_DATA_NOT_FOUND.getUserTip());
    }

    /**
     * 实体找不到
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new EntityNotFoundException(message);
        }
    }
}
