package org.jeecg.common.util.supersearch;

import lombok.Getter;
import org.jeecg.common.util.oConvertUtils;

/**
 * Query 规则 常量
 *
 * @Author Scott
 * @Date 2019年02月14日
 */
public enum QueryRuleEnum {

    /**
     * Query 规则 常量
     */
    GT(">", "大于"),
    GE(">=", "大于等于"),
    LT("<", "小于"),
    LE("<=", "小于等于"),
    EQ("=", "等于"),
    NE("!=", "不等于"),
    IN("IN", "包含"),
    LIKE("LIKE", "全模糊"),
    LEFT_LIKE("LEFT_LIKE", "左模糊"),
    RIGHT_LIKE("RIGHT_LIKE", "右模糊"),
    SQL_RULES("EXTEND_SQL", "自定义SQL片段");

    @Getter
    private String value;

    @Getter
    private String msg;

    QueryRuleEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static QueryRuleEnum getByValue(String value) {
        if (oConvertUtils.isEmpty(value)) {
            return null;
        }
        for (QueryRuleEnum val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
