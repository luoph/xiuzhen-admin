package org.jeecg.common.aspect.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 *
 * @Author taoyan
 * @Date 2019年4月11日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface PermissionData {
    /**
     * 配置菜单的组件路径,用于数据权限
     */
    String value() default "";
}