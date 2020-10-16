package cn.youai.xiuzhen.entity.pojo;


import lombok.Getter;

/**
 * (0,''已提交,未支付''),
 * (1,''已转发,未回复''),
 * (2,''已支付,金币发放中''),
 * (3,''金币发放中''),
 * (4,''充值成功,金币已发放'')
 *
 * @author luopeihuan
 */
public enum OrderStatus {
    /**
     * 订单状态
     */
    COMMIT("已提交,未支付", 0),
    PAID("已支付", 1),
    FORWARD("已转发,未回复", 2),
    ISSUING("金币发放中", 3),
    COMPLETE("充值成功,金币已发放", 4),
    ;

    /**
     * 成员变量
     */
    @Getter
    private String name;

    /**
     * 编码
     */
    @Getter
    private int type;

    OrderStatus(String name, int type) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        for (OrderStatus c : OrderStatus.values()) {
            if (c.getType() == type) {
                return c.getName();
            }
        }
        return null;
    }
}

