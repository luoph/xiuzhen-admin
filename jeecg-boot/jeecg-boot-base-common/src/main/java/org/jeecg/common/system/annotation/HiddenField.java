package org.jeecg.common.system.annotation;

import org.jeecg.common.constant.enums.ModuleType;

import java.lang.annotation.*;


/**
 * 隐藏字段，不传递到前端
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HiddenField {

}
