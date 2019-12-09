package org.jeecg.common.system.query;

import lombok.Getter;
import org.jeecg.common.util.oConvertUtils;

/**
 * Query 规则 常量
 *
 * @author Scott
 * @Date 2019年02月14日
 */
public enum QueryRuleEnum {

    /**
     * Query 规则 常量
     */
    GT(">", "gt", "大于"),
    GE(">=", "ge", "大于等于"),
    LT("<", "lt", "小于"),
    LE("<=", "le", "小于等于"),
    EQ("=", "eq", "等于"),
    NE("!=", "ne", "不等于"),
    IN("IN", "in", "包含"),
    LIKE("LIKE", "like", "全模糊"),
    LEFT_LIKE("LEFT_LIKE", "left_like", "左模糊"),
    RIGHT_LIKE("RIGHT_LIKE", "right_like", "右模糊"),
    SQL_RULES("USE_SQL_RULES", "ext", "自定义SQL片段");

    @Getter
    private String value;

    @Getter
    private String condition;

    @Getter
    private String msg;

    QueryRuleEnum(String value, String condition, String msg) {
        this.value = value;
        this.condition = condition;
        this.msg = msg;
    }

    public static QueryRuleEnum getByValue(String value) {
        if (oConvertUtils.isEmpty(value)) {
            return null;
        }
        for (QueryRuleEnum val : values()) {
            if (val.getValue().equals(value) || val.getCondition().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
