package com.boot.admin.annotation;

import com.boot.admin.util.enums.DataScopeFieldEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限过滤注解
 *
 * @author Li Yanfeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {

    /**
     * 关联表别名
     */
    String alias() default "";

    /**
     * 关联字段
     */
    DataScopeFieldEnum field();
}
