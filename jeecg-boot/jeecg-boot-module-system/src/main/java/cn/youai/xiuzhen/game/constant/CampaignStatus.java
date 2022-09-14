package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 活动状态
 * 0 - 已关闭
 * 1 - 未开始
 * 2 - 进行中
 * 3 - 已结束
 *
 * @author luopeihuan
 */
@Getter
public enum CampaignStatus {

    /**
     * 活动状态
     */
    NONE("未开启", -1),
    CLOSED("已关闭", 0),
    NOT_STARTED("未开始", 1),
    IN_PROGRESS("进行中", 2),
    COMPLETED("已结束", 3),
    ;

    private final String name;
    private final int value;

    CampaignStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
