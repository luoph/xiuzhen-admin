package cn.youai.xiuzhen.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @ClassName BigDecimalUtil
 * @Description 精确计算工具类
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-22 15:11
 */
public final class BigDecimalUtil {

    private static final int DEF_DIV_SCALE = 10;

    private BigDecimalUtil() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.add(b2);
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal subtract(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.subtract(b2);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal multiply(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal divide(double v1, double v2) {
        return divide(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    除数
     * @param v2    被除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = BigDecimal.valueOf(v);
        return b.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 被除数==0时 不抛异常的除法计算
     *
     * @param v1        除数
     * @param v2        被除数0 不抛出异常判定
     * @param isPercent 计算类型 true-百分比类型 false-正常小数值
     * @return !0 被除数计算结果
     */
    public static BigDecimal divideZero(double v1, double v2, boolean isPercent) {
        if (v2 != 0) {
            BigDecimal result = divide(v1, v2, 2);
            if (isPercent) {
                return result.multiply(BigDecimal.valueOf(100));
            }
            return result;
        }
        return BigDecimal.valueOf(0.00);
    }

    /**
     * 获取对应数值的BigDecimal对象
     *
     * @param v 获取的数值
     * @return 对应数值
     */
    public static BigDecimal valueOf(double v) {
        return BigDecimal.valueOf(v);
    }

    /**
     * 被除数==0时 不抛异常的除法计算
     *
     * @param v1        除数
     * @param v2        被除数0 不抛出异常判定
     * @param isPercent 计算类型 true-百分比类型 false-正常小数值
     * @return !0 被除数计算结果, 返回结果: 乘100,保留4位小数,
     */
    public static BigDecimal divideFour(double v1, double v2, boolean isPercent) {
        if (v2 != 0) {
            BigDecimal result = divide(v1, v2, 4);
            if (isPercent) {
                return result.multiply(BigDecimal.valueOf(100));
            }
            return result;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 返回百分数格式的数字
     *
     * @return 返回结果乘100,保留两位小数, 例: 0.1234 --> 12.34
     */
    public static BigDecimal dividePercent(double v1) {
    	if (v1 == 0){
    		return BigDecimal.ZERO;
	    }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        BigDecimal multiply = multiply(v1, 100);
        String format = decimalFormat.format(multiply);
        return new BigDecimal(format);
    }
    /**
     * 返回结果保留两位小数的BigDecimal对象
     *
     * @return 返回结果保留两位小数
     */
    public static BigDecimal divideTwo(double v1) {
	    if (v1 == 0){
		    return BigDecimal.ZERO;
	    }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
	    BigDecimal bigDecimal = valueOf(v1);
	    String format = decimalFormat.format(bigDecimal);
        return new BigDecimal(format);
    }


}
