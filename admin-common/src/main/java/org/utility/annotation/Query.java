package org.utility.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询 注解
 *
 * @author Li Yanfeng
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * 基本对象的属性名
     */
    String propName() default "";

    /**
     * 查询方式
     */
    Type type() default Type.EQ;

    /**
     * 默认左连接
     */
    Join join() default Join.LEFT;

    /**
     * 连接查询的属性名，如User类中的dept
     */
    String joinName() default "";

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如 @Query(blurry = "email,username")
     */
    String blurry() default "";


    /**
     * SQL 关键字枚举
     */
    enum Type {
        // 等于
        EQ,
        // 不等于
        NE,
        // 大于等于
        GT,
        // 大于
        GE,
        // 小于等于
        LT,
        // 小于
        LE,
        // 并且
        AND,
        // 或者
        OR,
        // 包含
        IN,
        // 不包含
        NOT_IN,
        // 中模糊
        LIKE,
        // 左模糊
        LEFT_LIKE,
        // 右模糊
        RIGHT_LIKE,
        // 为空
        IS_NULL,
        // 不为空
        IS_NOT_NULL,
        // Between
        BETWEEN,
        // 升序
        ASC,
        // 降序
        DESC
    }

    /**
     * SQL 连接方式枚举
     */
    enum Join {
        // 内连接
        INNER,
        // 左外连接
        LEFT,
        // 右外连接
        RIGHT
    }
}
