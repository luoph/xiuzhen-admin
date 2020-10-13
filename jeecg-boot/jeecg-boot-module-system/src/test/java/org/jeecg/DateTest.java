package org.jeecg;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/10 12:28
 */
public class DateTest {
    

    public static void main(String[] args) {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date pastDate = DateUtils.getPastDate(7, sdf);
        System.out.println(pastDate);*/
        BigDecimal a =null;
        BigDecimal b =null;
        //Integer faultRate = 1;
        //a = BigDecimal.valueOf(faultRate.doubleValue()/2);
        a = BigDecimal.valueOf(0.123456987);
        b = BigDecimal.valueOf(0.323456987);

        System.out.println("开始是"+a);
        //BigDecimal  b =a.setScale(2, RoundingMode.HALF_UP);//保留两位小数
       //System.out.println("结果是"+b);
        //下面将结果转化成百分比
        //NumberFormat percent = NumberFormat.getPercentInstance();
        //percent.setMaximumFractionDigits(2);
        //String xx = percent.format(b.doubleValue());
        BigDecimal cc = BigDecimalUtil.divideZero(a.doubleValue(), b.doubleValue(),true);
        //System.out.println(percent.format(b.doubleValue()));
        System.out.println(cc);



    }
}


