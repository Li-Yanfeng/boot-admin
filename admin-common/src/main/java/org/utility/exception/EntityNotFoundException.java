package org.utility.exception;

import org.springframework.util.StringUtils;

/**
 * 实体找不到异常
 *
 * @author liYanfeng
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String field, String val) {
        super(generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity) + " with " + field + " " + val + " does not exist";
    }
}
