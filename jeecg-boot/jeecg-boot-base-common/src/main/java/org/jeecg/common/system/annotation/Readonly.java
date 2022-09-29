package org.jeecg.common.system.annotation;

import java.lang.annotation.*;

/**
 * 只读
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Readonly {

}

