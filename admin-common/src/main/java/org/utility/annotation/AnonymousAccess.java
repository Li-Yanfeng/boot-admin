package org.utility.annotation;

import java.lang.annotation.*;

/**
 * 匿名访问注解
 *
 * @author Li Yanfeng
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {
}
