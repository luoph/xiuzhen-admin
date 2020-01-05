package cn.youai.xiuzhen.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * 数字工具类
 *
 * @author luopeihuan
 */
@Slf4j
public class NumberUtils {
    private static final float EPSILON = 0.00001f;
    /**
     * 小数进度
     */
    private static final int SCALE_PRECISION = 2;

    private static final Set<String> NUMBER_CLASSES = new HashSet<>(10);

    static {
        NUMBER_CLASSES.add(Boolean.class.getName());
        NUMBER_CLASSES.add(Byte.class.getName());
        NUMBER_CLASSES.add(Short.class.getName());
        NUMBER_CLASSES.add(Integer.class.getName());
        NUMBER_CLASSES.add(Long.class.getName());
        NUMBER_CLASSES.add(Float.class.getName());
        NUMBER_CLASSES.add(Double.class.getName());
        NUMBER_CLASSES.add(BigDecimal.class.getName());
        NUMBER_CLASSES.add(BigInteger.class.getName());
    }


    /**
     * 转成int型
     *
     * @param str
     * @return
     */
    public static int toInt32(String str) {
        return Double.valueOf(str).intValue();
    }

    /**
     * 转成int型
     *
     * @param obj
     * @return
     */
    public static int toInt32(Object obj) {
        return Double.valueOf(obj.toString()).intValue();
    }

    /**
     * 转成long型
     *
     * @param str
     * @return
     */
    public static long toInt64(String str) {
        return Long.parseLong(str);
    }

    /**
     * 转成double
     *
     * @param str
     * @return
     */
    public static double toDouble(String str) {
        return Double.parseDouble(str);
    }

    /**
     * 转成double
     *
     * @param obj
     * @return
     */
    public static double toDouble(Object obj) {
        return Double.parseDouble(obj.toString());
    }

    /**
     * 判断是否数值类型
     *
     * @param clazz
     * @return
     */
    public static boolean isNumber(Class clazz) {
        return NUMBER_CLASSES.contains(clazz.getName());
    }

    /**
     * 判断是否非0值
     *
     * @param number
     * @return
     */
    public static boolean isNotZero(Number number) {
        return !isZero(number);
    }

    /**
     * 判断是否值为0
     *
     * @param number
     * @return
     */
    public static boolean isZero(Number number) {
        if (number == null) {
            return true;
        }

        if (number instanceof Byte) {
            return (Byte) number == 0;
        } else if (number instanceof Short) {
            return (Short) number == 0;
        } else if (number instanceof Integer) {
            return (Integer) number == 0;
        } else if (number instanceof Long) {
            return (Long) number == 0;
        } else if (number instanceof Float) {
            return (((Float) number >= -EPSILON) && ((Float) number <= EPSILON));
        } else if (number instanceof Double) {
            return (((Double) number >= -EPSILON) && ((Double) number <= EPSILON));
        } else if (number instanceof BigDecimal) {
            return BigDecimal.ZERO.compareTo((BigDecimal) number) == 0;
        } else if (number instanceof BigInteger) {
            return BigInteger.ZERO.compareTo((BigInteger) number) == 0;
        }
        return false;
    }

    /**
     * 生成0值的包装对象
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T createZero(Class<T> clazz) {
        if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) Boolean.FALSE;
        } else if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(0);
        } else if (clazz == double.class || clazz == Double.class) {
            return (T) Double.valueOf(0);
        } else if (clazz == byte.class || clazz == Byte.class) {
            return (T) Byte.valueOf((byte) 0);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(0L);
        } else if (clazz == short.class || clazz == Short.class) {
            return (T) Short.valueOf((short) 0);
        } else if (clazz == float.class || clazz == Float.class) {
            return (T) Float.valueOf(0);
        } else if (clazz == BigDecimal.class) {
            return (T) BigDecimal.ZERO;
        } else if (clazz == BigInteger.class) {
            return (T) BigInteger.ZERO;
        }
        return null;
    }

    /**
     * 构建数值类型
     *
     * @param resultType
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T valueOf(Class<T> resultType, Number value) {
        if (resultType == null) {
            String msg = value.getClass().getSimpleName() + " -> NULL";
            throw new NullPointerException(msg);
        }

        if (resultType == boolean.class || resultType == Boolean.class) {
            return (T) Boolean.valueOf(value.doubleValue() > 0D);
        } else if (resultType == int.class || resultType == Integer.class) {
            return (T) Integer.valueOf(value.intValue());
        } else if (resultType == double.class || resultType == Double.class) {
            return (T) Double.valueOf(value.doubleValue());
        } else if (resultType == byte.class || resultType == Byte.class) {
            return (T) Byte.valueOf(value.byteValue());
        } else if (resultType == long.class || resultType == Long.class) {
            return (T) Long.valueOf(value.longValue());
        } else if (resultType == short.class || resultType == Short.class) {
            return (T) Short.valueOf(value.shortValue());
        } else if (resultType == float.class || resultType == Float.class) {
            return (T) Float.valueOf(value.floatValue());
        } else if (resultType == BigDecimal.class) {
            return (T) new BigDecimal(String.valueOf(value));
        } else if (resultType == BigInteger.class) {
            return (T) new BigInteger(String.valueOf(value));
        } else if (resultType == Number.class) {
            return (T) value;
        } else {
            String msg = value.getClass().getSimpleName() + " -> " + resultType.getSimpleName();
            throw new IllegalArgumentException(new ClassCastException(msg));
        }
    }


    /**
     * 加法计算
     *
     * @param resultType 返回类型
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T add(Class<T> resultType, Number a, Number b) {
        if (resultType == int.class || resultType == Integer.class) {
            return (T) Integer.valueOf(a.intValue() + b.intValue());
        } else if (resultType == double.class || resultType == Double.class) {
            return (T) (Double) add(a.doubleValue(), b.doubleValue());
        } else if (resultType == byte.class || resultType == Byte.class) {
            return (T) Byte.valueOf((byte) (a.byteValue() + b.byteValue()));
        } else if (resultType == long.class || resultType == Long.class) {
            return (T) Long.valueOf(a.longValue() + b.longValue());
        } else if (resultType == short.class || resultType == Short.class) {
            return (T) Short.valueOf((short) (a.shortValue() + b.shortValue()));
        } else if (resultType == float.class || resultType == Float.class) {
            return (T) (Float) add(a.floatValue(), b.floatValue());
        } else if (resultType == BigDecimal.class) {
            return (T) add(BigDecimal.valueOf(a.doubleValue()), BigDecimal.valueOf(b.doubleValue()));
        } else if (resultType == BigInteger.class) {
            return (T) BigInteger.valueOf(a.longValue()).add(BigInteger.valueOf(b.longValue()));
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static <T> T subtract(Class<T> resultType, Number a, Number b) {
        if (resultType == int.class || resultType == Integer.class) {
            return (T) Integer.valueOf(a.intValue() - b.intValue());
        } else if (resultType == double.class || resultType == Double.class) {
            return (T) (Double) subtract(a.doubleValue(), b.doubleValue());
        } else if (resultType == byte.class || resultType == Byte.class) {
            return (T) Byte.valueOf((byte) (a.byteValue() - b.byteValue()));
        } else if (resultType == long.class || resultType == Long.class) {
            return (T) Long.valueOf(a.longValue() - b.longValue());
        } else if (resultType == short.class || resultType == Short.class) {
            return (T) Short.valueOf((short) (a.shortValue() - b.shortValue()));
        } else if (resultType == float.class || resultType == Float.class) {
            return (T) (Float) subtract(a.floatValue(), b.floatValue());
        } else if (resultType == BigDecimal.class) {
            return (T) subtract(BigDecimal.valueOf(a.doubleValue()), BigDecimal.valueOf(b.doubleValue()));
        } else if (resultType == BigInteger.class) {
            return (T) BigInteger.valueOf(a.longValue()).subtract(BigInteger.valueOf(b.longValue()));
        }
        return null;
    }

    /**
     * 加法计算
     *
     * @param value 原值
     * @param delta 增加的值
     * @return 加法计算结果值
     */
    public static Number add(Number value, Number delta) {
        return add(value.getClass(), value, delta);
    }


    /**
     * 减法计算
     *
     * @param a
     * @param b
     * @return 减法结果
     */
    public static Number subtract(Number a, Number b) {
        return subtract(a.getClass(), a, b);
    }

    /**
     * 判断两个数值是否相同
     *
     * @param fieldType
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T> boolean equals(Class<T> fieldType, Number a, Number b) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return a.intValue() == b.intValue();
        } else if (fieldType == double.class || fieldType == Double.class) {
            double diff = a.doubleValue() - b.doubleValue();
            return ((diff >= -EPSILON) && (diff <= EPSILON));
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            return a.byteValue() == b.byteValue();
        } else if (fieldType == long.class || fieldType == Long.class) {
            return a.longValue() == b.longValue();
        } else if (fieldType == short.class || fieldType == Short.class) {
            return a.shortValue() == b.shortValue();
        } else if (fieldType == float.class || fieldType == Float.class) {
            float diff = a.floatValue() - b.floatValue();
            return ((diff >= -EPSILON) && (diff <= EPSILON));
        } else if (fieldType == BigDecimal.class) {
            return BigDecimal.valueOf(a.doubleValue()).compareTo(BigDecimal.valueOf(b.doubleValue())) == 0;
        } else if (fieldType == BigInteger.class) {
            return BigInteger.valueOf(a.longValue()).compareTo(BigInteger.valueOf(b.longValue())) == 0;
        }

        return StringUtils.equals(a.toString(), b.toString());
    }

    /**
     * 乘法计算
     *
     * @param resultType 返回类型
     * @param value
     * @param num
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T multiply(Class<T> resultType, Number value, int num) {
        if (resultType == int.class || resultType == Integer.class) {
            return (T) Integer.valueOf(value.intValue() * num);
        } else if (resultType == double.class || resultType == Double.class) {
            return (T) Double.valueOf(value.doubleValue() * num);
        } else if (resultType == byte.class || resultType == Byte.class) {
            return (T) Byte.valueOf((byte) (value.byteValue() * num));
        } else if (resultType == long.class || resultType == Long.class) {
            return (T) Long.valueOf(value.longValue() * num);
        } else if (resultType == short.class || resultType == Short.class) {
            return (T) Short.valueOf((short) (value.shortValue() * num));
        } else if (resultType == float.class || resultType == Float.class) {
            return (T) Float.valueOf(value.floatValue() * num);
        } else if (resultType == BigDecimal.class) {
            return (T) BigDecimal.valueOf(value.doubleValue()).multiply(BigDecimal.valueOf(num));
        } else if (resultType == BigInteger.class) {
            return (T) BigInteger.valueOf(value.longValue()).multiply(BigInteger.valueOf(num));
        }
        return null;
    }

    /**
     * 乘法计算
     *
     * @param resultType 返回类型
     * @param value
     * @param num
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T multiply(Class<T> resultType, Number value, double num) {
        if (resultType == int.class || resultType == Integer.class) {
            return (T) Integer.valueOf((int) (value.intValue() * num));
        } else if (resultType == double.class || resultType == Double.class) {
            return (T) (Double) round(value.doubleValue() * num);
        } else if (resultType == byte.class || resultType == Byte.class) {
            return (T) Byte.valueOf((byte) (value.byteValue() * num));
        } else if (resultType == long.class || resultType == Long.class) {
            return (T) Long.valueOf((long) (value.longValue() * num));
        } else if (resultType == short.class || resultType == Short.class) {
            return (T) Short.valueOf((short) (value.shortValue() * num));
        } else if (resultType == float.class || resultType == Float.class) {
            return (T) (Float) round((float) (value.floatValue() * num));
        } else if (resultType == BigDecimal.class) {
            return (T) round(BigDecimal.valueOf(value.doubleValue()).multiply(BigDecimal.valueOf(num)));
        } else if (resultType == BigInteger.class) {
            return (T) BigInteger.valueOf(value.longValue()).multiply(BigInteger.valueOf((long) num));
        }
        return null;
    }

    /**
     * 乘法计算
     *
     * @param value 原值
     * @param num   增加的值
     * @return 加法计算结果值
     */
    public static Number multiply(Number value, int num) {
        return multiply(value.getClass(), value, num);
    }

    /**
     * 乘法计算
     *
     * @param value 原值
     * @param num   增加的值
     * @return 加法计算结果值
     */
    public static Number multiply(Number value, double num) {
        return multiply(value.getClass(), value, num);
    }

    /**
     * 拷贝属性
     *
     * @param resultType 返回类型
     * @param value      源数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(Class<T> resultType, Number value) {
        if (value == null) {
            return null;
        }

        if (resultType == boolean.class || resultType == Boolean.class) {
            return (T) Boolean.valueOf(value.doubleValue() > 0D);
        } else if (resultType == int.class || resultType == Integer.class) {
            return (T) Integer.valueOf(value.intValue());
        } else if (resultType == double.class || resultType == Double.class) {
            return (T) Double.valueOf(value.doubleValue());
        } else if (resultType == byte.class || resultType == Byte.class) {
            return (T) Byte.valueOf(value.byteValue());
        } else if (resultType == long.class || resultType == Long.class) {
            return (T) Long.valueOf(value.longValue());
        } else if (resultType == short.class || resultType == Short.class) {
            return (T) Short.valueOf(value.shortValue());
        } else if (resultType == float.class || resultType == Float.class) {
            return (T) Float.valueOf(value.floatValue());
        } else if (resultType == BigDecimal.class) {
            return (T) new BigDecimal(String.valueOf(value));
        } else if (resultType == BigInteger.class) {
            return (T) new BigInteger(String.valueOf(value));
        } else if (resultType == Number.class) {
            return (T) value;
        }

        return (T) value;
    }

    /**
     * 将字符串按指定的分割符分割并返回指定类型的数组
     *
     * @param element   字符串
     * @param separator 分割符
     * @param clazz     要转换类型的类,如:Integer.class,Long.class
     * @return T[] 返回class类型的数组
     */
    public static <T> T[] convertArray(String element, String separator, Class<T> clazz) {
        if (element != null && element.trim().length() > 0) {
            String[] values = element.split(separator);
            try {
                return covertArray(clazz, values, 0, values.length);
            } catch (Exception e) {
                log.error("convertArray error, element:" + element + ", separator:" + separator + ", class:" + clazz.getName(), e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static <T> T[] covertArray(Class<T> clazz, String[] vals, int from, int to)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        int start, end;
        if (from > to) {
            start = to;
            end = from;
        } else {
            start = from;
            end = to;
        }

        T[] result = (T[]) Array.newInstance(clazz, to - from);
        Method valueOfMethod = clazz.getMethod("valueOf", String.class);
        if (valueOfMethod != null) {
            boolean accessible = valueOfMethod.isAccessible();
            valueOfMethod.setAccessible(true);
            for (int i = start; i < end; i++) {
                T val = (T) valueOfMethod.invoke(clazz, vals[i]);
                result[i - start] = val;
            }
            valueOfMethod.setAccessible(accessible);
        }
        return result;
    }

    /**
     * 限制小数进度，使用4舍5入
     *
     * @param num
     * @return
     */
    public static BigDecimal round(BigDecimal num) {
        return num.setScale(SCALE_PRECISION, BigDecimal.ROUND_HALF_UP);
    }

    public static double round(double num) {
        return BigDecimal.valueOf(num).setScale(SCALE_PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static float round(float num) {
        return BigDecimal.valueOf(num).setScale(SCALE_PRECISION, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    public static double subtract(double a, double b) {
        return BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).doubleValue();
    }

    public static float subtract(float a, float b) {
        return BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).floatValue();
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }
    
    public static BigDecimal add(BigDecimal a, BigDecimal... bds) {
    	if (null == bds || bds.length == 0) {
    		return a;
    	}
    	BigDecimal total = a;
    	for (BigDecimal b : bds) {
    		total = total.add(b);
    	}
        return total;
    }

    public static double add(double a, double b) {
        return BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).doubleValue();
    }

    public static float add(float a, float b) {
        return BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).floatValue();
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }
    
    public static double multiply(double a, double b) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).doubleValue();
    }

    public static float multiply(float a, float b) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).floatValue();
    }
    
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b);
    }
    
    // 生成 [min, max] 区间内随机整数
	public static int getReandomInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}
}
