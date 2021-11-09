package org.utility.exception;

import org.springframework.util.StringUtils;

/**
 * 实体存在异常
 *
 * @author LiYanfeng
 */
public class EntityExistException extends RuntimeException {

    public EntityExistException(Class clazz, String field, String val) {
        super(generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity) + " with " + field + " " + val + " existed";
    }
}
