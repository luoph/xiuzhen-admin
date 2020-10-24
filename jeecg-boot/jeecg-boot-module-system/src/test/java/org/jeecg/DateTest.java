package org.jeecg;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/10 12:28
 */
public class DateTest {


	public static void main(String[] args) {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(pastDate);*/
		BigDecimal a = null;
		BigDecimal b = null;
		//Integer faultRate = 1;
		//a = BigDecimal.valueOf(faultRate.doubleValue()/2);
		a = BigDecimal.valueOf(0.8000);
		b = BigDecimal.valueOf(0.4000);

		//System.out.println("开始是"+a);
		//BigDecimal  b =a.setScale(2, RoundingMode.HALF_UP);//保留两位小数
		//System.out.println("结果是"+b);
		//下面将结果转化成百分比
		//NumberFormat percent = NumberFormat.getPercentInstance();
		//percent.setMaximumFractionDigits(2);
		//String xx = percent.format(b.doubleValue());
		BigDecimal cc = BigDecimalUtil.divideZero(a.doubleValue(), b.doubleValue(), true);
		BigDecimal bigDecimal = BigDecimalUtil.divideFour(1, 10, false);
		System.out.println(bigDecimal);
		//System.out.println(percent.format(b.doubleValue()));
		//System.out.println(cc);
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class Mqtt {
		private int key;
		private int value;
		private Integer groupNo;

	}

	@Test
	public void test() {
		List<Mqtt> list = new ArrayList<>();
		list.add(new Mqtt(2, 2, 8));
		list.add(new Mqtt(5, 7, 8));
		list.add(new Mqtt(2, 5, 7));
		list.add(new Mqtt(3, 9, 7));
		list.add(new Mqtt(2, 4, 6));
		list.add(new Mqtt(2, 1, 6));
		list.add(new Mqtt(2, 2, 6));
		list.add(new Mqtt(3, 1, 6));
		list.add(new Mqtt(3, 4, 6));

		Map<Integer, List<Mqtt>> map = list.stream().collect(Collectors.groupingBy(Mqtt::getGroupNo));
		for (List<Mqtt> mqtts : map.values()) {
			long count = mqtts.stream().filter(i -> i.getKey() == 2).count();
			Integer max = mqtts.stream().filter(i -> i.getKey() == 2).max(Comparator.comparing(Mqtt::getValue)).map(Mqtt::getValue).orElse(0);

			// int max = maxTemperature.get().getValue();
			long sum = mqtts.stream().filter(i -> i.getKey() == 2).mapToLong(Mqtt:: getValue).sum();

			System.out.println("该组的最大值"+ max);
			System.out.println("该组的count个数" + count);
			System.out.println("该组的sum和" + sum);
		}

	}
}


