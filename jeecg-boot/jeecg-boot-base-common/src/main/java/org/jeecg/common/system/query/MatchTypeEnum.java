package org.jeecg.common.system.query;

import lombok.Getter;
import org.jeecg.common.util.oConvertUtils;

/**
 * 查询链接规则
 *
 * @author Sunjianlei
 */
public enum MatchTypeEnum {

    /**
     * 查询链接规则
     */
    AND("AND"),
    OR("OR");

    @Getter
    private String value;

    MatchTypeEnum(String value) {
        this.value = value;
    }

    public static MatchTypeEnum getByValue(String value) {
        if (oConvertUtils.isEmpty(value)) {
            return null;
        }
        for (MatchTypeEnum val : values()) {
            if (val.getValue().toLowerCase().equals(value.toLowerCase())) {
                return val;
            }
        }
        return null;
    }
}
