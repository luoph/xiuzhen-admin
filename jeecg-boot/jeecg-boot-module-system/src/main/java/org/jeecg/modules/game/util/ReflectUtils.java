package org.jeecg.modules.game.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 2014-2015 by 广州游爱 Inc.
 *
 * @author Create by Kim_On
 * @date 2014-7-26 上午9:44:20
 * @Description
 */
@Slf4j
public class ReflectUtils {

    public static Object invokeGetField(Object o, String field) {
        Field declaredField;
        try {
            declaredField = o.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            return declaredField.get(o);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.error("[invokeGetField] error, o:" + o + ", field:" + field, e);
        }

        return null;
    }

    public static void invokeSetField(Object o, String field, double params) {
        Field declaredField;
        try {
            declaredField = o.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredField.set(o, BigDecimal.valueOf(params));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.error("[invokeGetField] error, o:" + o + ", field:" + field + ", params:" + params, e);
        }
    }

    @SuppressWarnings("JavaReflectionInvocation")
    public static Object invoke(Object o, String method, Object... params) {
        try {
            Method m = o.getClass().getMethod(method);
            return m.invoke(o, params);
        } catch (Exception e) {
            log.error("[invokeGetField] error, o:" + o + ", method:" + method + ", params:" + params, e);
        }
        return null;
    }

    /**
     * 获取静态变量值
     *
     * @param clazz       类
     * @param staticField 静态字段
     * @return 静态变量值
     */
    public static Object fetchStaticValue(Class<?> clazz, String staticField) {
        try {
            return clazz.getDeclaredField(staticField).get(null);
        } catch (Exception e) {
            log.error("[fetchStaticValue] error, clazz:" + clazz + ", staticField:" + staticField, e);
        }
        return null;
    }

    /**
     * 获取静态变量值
     *
     * @param clazz        类
     * @param staticMethod 静态方法
     * @return {@linkplain Object}
     */
    @SuppressWarnings("JavaReflectionInvocation")
    public static Object fetchStaticMethod(Class<?> clazz, String staticMethod, Object... params) {
        try {
            Method declaredMethod = clazz.getDeclaredMethod(staticMethod);
            return declaredMethod.invoke(null, params);
        } catch (Exception e) {
            log.error("[fetchStaticMethod] error, clazz:" + clazz + ", staticField:" + staticMethod, e);
        }
        return null;
    }

    /**
     * 获取模板类型
     *
     * @param clazz 类
     * @return {@linkplain Type}
     */
    public static Type templateType(Object clazz) {
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getClass().getGenericSuperclass();
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return p.getRawType();
        } else {
            return type;
        }
    }

    /**
     * 获取包含父类在内的所有字段
     *
     * @param fields 字段列表
     * @param type   类型
     */
    public static void getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
    }
}
