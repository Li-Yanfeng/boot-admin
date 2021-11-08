package org.utility.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于判断是否过滤数据权限
 * 1、如果没有用到 @OneToOne 这种关联关系，只需要填写 fieldName [参考：DeptQuery.class]
 * 2、如果用到了 @OneToOne ，fieldName 和 joinName 都需要填写，拿 UserQuery.class 举例:
 * 应该是 @DataPermission(joinName = "dept", fieldName = "id")
 *
 * @author Li Yanfeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {

    /**
     * Entity 中与部门关联的字段名称
     */
    String joinName() default "";

    /**
     * Entity 中的字段名称
     */
    String fieldName() default "";

    /**
     * 忽略场景配置
     * 与 @Scene 配合使用
     */
    String[] ignoreScene() default {};
}
