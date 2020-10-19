package cn.youai.xiuzhen.entity.pojo;


import lombok.Getter;

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
    INCREASE(1, "增加"),
    REDUCE(2, "减少");

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
}

