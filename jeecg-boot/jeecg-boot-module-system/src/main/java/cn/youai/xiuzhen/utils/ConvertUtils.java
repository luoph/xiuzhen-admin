package cn.youai.xiuzhen.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author luopeihuan
 */
@Slf4j
public class ConvertUtils {

    /***
     * 类型转化
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> convertList(List<?> list, Class<T> clazz) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> resultList = new ArrayList<>();
        for (Object source : list) {
            T target = createNewInstance(clazz);
            if (target != null) {
                ConvertUtils.copyProperties(source, target);
                resultList.add(target);
            }
        }
        return resultList;
    }


    private static <T> T createNewInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            log.error("class newInstance error, class:" + clazz.getName(), ex);
        }
        return null;
    }

    /**
     * 初始化包装对象的默认值为0
     *
     * @param target
     * @param <T>
     */
    public static <T> void initialNumberField(T target) {
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            Method readMethod = targetPd.getReadMethod();
            if (writeMethod == null || readMethod == null) {
                continue;
            }

            if (NumberUtils.isNumber(readMethod.getReturnType())) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(target);

                    if (value == null) {
                        value = NumberUtils.createZero(readMethod.getReturnType());
                        if (value != null) {
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    }
                } catch (Throwable ex) {
                    log.error("initialNumberField error, class:" + target.getClass(), ex);
                }
            }
        }
    }

    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, (String[]) null);
    }

    /**
     * 拷贝属性
     *
     * @param source           源数据
     * @param target           目标数据
     * @param ignoreProperties 忽略的属性
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object sourceValue = readMethod.invoke(source);

                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }

                            if (NumberUtils.isNumber(readMethod.getReturnType())) {
                                Object targetValue = NumberUtils.copy(writeMethod.getParameterTypes()[0], (Number) sourceValue);
                                writeMethod.invoke(target, targetValue);
                            } else {
                                writeMethod.invoke(target, sourceValue);
                            }

                        } catch (Throwable ex) {
                            throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }


    /**
     * 初始化包装对象的默认值为0
     *
     * @param target
     * @param <T>
     */
    public static <T> void initialPrimitiveKey(JSONObject target, Class<T> clazz) {
        Field[] allFields = FieldUtils.getAllFields(clazz);
        for (Field field : allFields) {
            // 忽略静态成员
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String property = field.getName();
            JSONField jsonField = field.getAnnotation(JSONField.class);
            if (jsonField != null) {
                property = jsonField.name();
            }

            Class<?> fieldType = field.getType();
            if (fieldType.isArray() || List.class.isAssignableFrom(fieldType)) {
                Object value = target.get(property);
                if (value instanceof JSONArray) {
                    continue;
                }

                // 处理 json array 空值
                if (!(value instanceof String) || StringUtils.isBlank((String) value)) {
                    target.put(property, new JSONArray());
                }
            } else if (NumberUtils.isNumber(fieldType)) {
                Object value = target.get(property);
                if (value == null || (value instanceof String && StringUtils.isBlank((String) value))) {
                    value = NumberUtils.createZero(field.getType());
                    target.put(property, value);
                }
            }
        }
    }

    public static String safeString(String string) {
        return string != null ? string : StringUtils.EMPTY;
    }

    public static String safeString(String string, String fallback) {
        return string != null ? string : fallback;
    }

    public static int safeInteger(Integer num) {
        return null != num ? num : 0;
    }

    public static long safeLong(Long num) {
        return null != num ? num : 0;
    }
}
