package com.boot.admin.util;

import cn.hutool.core.util.ObjectUtil;
import com.boot.admin.exception.BadRequestException;

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
}
