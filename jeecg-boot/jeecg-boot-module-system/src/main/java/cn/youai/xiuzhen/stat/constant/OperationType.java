package cn.youai.xiuzhen.stat.constant;


import cn.youai.server.constant.ItemReduce;
import cn.youai.server.constant.ItemRuleId;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作类型
 *
 * @author luopeihuan
 */
@Getter
public enum OperationType {
    /**
     * 操作类型
     */
    INCREASE(1, "增加"), REDUCE(2, "减少");

    private final String name;
    private final int type;

    OperationType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        for (OperationType c : OperationType.values()) {
            if (c.getType() == type) {
                return c.getName();
            }
        }
        return null;
    }

    public static OperationType valueOf(int type) {
        for (OperationType c : OperationType.values()) {
            if (c.getType() == type) {
                return c;
            }
        }
        return null;
    }

    public static Map<Integer, String> getWayMap(int type) {
        Map<Integer, String> wayMap = null;
        if (OperationType.INCREASE.type == type) {
            wayMap = Arrays.stream(ItemRuleId.values()).collect(Collectors.toMap(ItemRuleId::getType, ItemRuleId::getName, (key1, key2) -> key2));
        } else if (OperationType.REDUCE.type == type) {
            wayMap = Arrays.stream(ItemReduce.values()).collect(Collectors.toMap(ItemReduce::getType, ItemReduce::getName, (key1, key2) -> key2));
        }
        return wayMap;
    }
}

