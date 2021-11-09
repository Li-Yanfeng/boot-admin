package org.utility.util;

import cn.hutool.core.util.ObjectUtil;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.utility.exception.BadRequestException;

/**
 * 验证工具
 *
 * @author Li Yanfeng
 */
public class ValidationUtils {

    /**
     * 验证不为空
     */
    public static void notNull(Object obj, String entity, String parameter, Object value) {
        if (ObjectUtil.isNull(obj)) {
            String msg = entity + " 不存在: " + parameter + " is " + value;
            throw new BadRequestException(msg);
        }
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }
}
