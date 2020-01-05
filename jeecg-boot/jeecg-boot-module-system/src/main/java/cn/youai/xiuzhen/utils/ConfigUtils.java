package cn.youai.xiuzhen.utils;

import cn.youai.xiuzhen.common.enums.RealmLevel;
import cn.youai.xiuzhen.common.enums.RealmSubLevel;
import cn.youai.xiuzhen.thirdparty.astar.Coordinate;
import cn.youai.xiuzhen.entity.common.WeightValuable;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author luopeihuan
 */
@Slf4j
public final class ConfigUtils {
    private static final int RANDOM_ROUND = 10;

    private static final String JSON_ARRAY_BRACKET_START = "[";
    private static final String JSON_ARRAY_BRACKET_END = "]";

    private ConfigUtils() {
    }

    /**
     * 从数组中获取随机索引
     *
     * @param array
     * @return
     */
    public static int genRandomIndex(JSONArray array) {
        return genRandomIndex(array.size());
    }

    /**
     * 从数组中获取随机索引
     *
     * @param array
     * @return
     */
    public static int genRandomIndex(String[] array) {
        return genRandomIndex(array.length);
    }

    /**
     * 获取随机值
     *
     * @param itemList
     * @param <T>
     * @return
     */
    public static <T> T getRandomItem(List<T> itemList) {
        if (itemList == null || itemList.size() == 0) {
            return null;
        }

        int index = genRandomIndex(itemList.size());
        return itemList.get(index);
    }

    /**
     * 返回 [0, size-1] 的随机数
     *
     * @param size
     * @return
     */
    public static int genRandomIndex(int size) {
        if (size == 0) {
            return 0;
        }
        int random = RandomUtils.nextInt(0, size * RANDOM_ROUND);
        return random % size;
    }

    /**
     * 解析坐标
     *
     * @param coord
     * @return
     */
    public static Coordinate parseCoord(String coord) {
        coord = StringUtils.substringBetween(coord, JSON_ARRAY_BRACKET_START, JSON_ARRAY_BRACKET_END);
        String[] array = coord.split(",");
        byte x = Byte.parseByte(array[0].trim());
        byte y = Byte.parseByte(array[1].trim());
        return new Coordinate((byte) (x - 1), (byte) (y - 1));
    }

    /**
     * 判断是否json数组
     *
     * @param input
     * @return
     */
    public static boolean isJSONArray(String input) {
        return StringUtils.startsWith(input, JSON_ARRAY_BRACKET_START)
                && StringUtils.endsWith(input, JSON_ARRAY_BRACKET_END);
    }

    /**
     * 根据配置的权重返回随机值
     *
     * @param itemList
     * @return
     */
    public static WeightValuable getRandomWeightItem(List<? extends WeightValuable> itemList) {
        if (itemList == null || itemList.size() == 0) {
            return null;
        }

        int totalWeight = 0;
        for (WeightValuable item : itemList) {
            int itemWeight = item.weightValue() != null ? item.weightValue() : 0;
            totalWeight += itemWeight;
        }

        // 生成随机数
        int randomWeight = RandomUtils.nextInt(0, totalWeight + 1);
        for (WeightValuable item : itemList) {
            randomWeight -= (item.weightValue() != null ? item.weightValue() : 0);
            if (randomWeight <= 0) {
                return item;
            }
        }

        return itemList.get(0);
    }

    /**
     * 获取修为等级描述
     *
     * @param scale 阶数
     * @param level 等级
     * @return
     */
    public static String getRealmGrade(int scale, int level) {
        return RealmLevel.getName(scale) + RealmSubLevel.getName(level);
    }

    public static boolean contains(JSONArray jsonArray, Integer id) {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.getInteger(i).equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(JSONArray jsonArray, String text) {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.getString(i).equals(text)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 拼接字符串
     *
     * @param separator
     * @param objects
     * @return
     */
    public static String joinWith(String separator, List<String> objects) {
        final StringBuilder result = new StringBuilder();
        final Iterator<String> iterator = objects.iterator();
        while (iterator.hasNext()) {
            final String value = Objects.toString(iterator.next(), "");
            result.append(value);

            if (iterator.hasNext()) {
                result.append(separator);
            }
        }
        return result.toString();
    }


}
