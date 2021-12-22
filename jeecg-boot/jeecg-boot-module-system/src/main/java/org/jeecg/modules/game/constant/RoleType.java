package org.jeecg.modules.game.constant;

import lombok.Getter;

/**
 * 玩家类型
 */
@Getter
public enum RoleType {
    /**
     * 统计类型
     */
    ALL("所有玩家", 0),
    PAID("付费玩家", 1),
    FREE("免费玩家", 2),
    ;

    private final String name;
    private final int value;

    RoleType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static RoleType valueOf(int type) {
        for (RoleType e : RoleType.values()) {
            if (e.getValue() == type) {
                return e;
            }
        }
        return ALL;
    }
}
