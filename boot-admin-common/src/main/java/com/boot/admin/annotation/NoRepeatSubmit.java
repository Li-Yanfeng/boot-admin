package com.boot.admin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限制数据在一定时间内重复提交
 *
 * @author Li Yanfeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /**
     * 默认时间10秒
     */
    int time() default 10 * 1000;
}
