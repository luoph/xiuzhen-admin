package cn.youai.xiuzhen.game.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 玩家行为次数统计类
 *
 * @author huli
 */
@Data
@Accessors(chain = true)
public class PlayerLogTypeTimes {
    /**
     * 玩家id
     */
    private Long playerId;
    /**
     * 玩家行为名称
     */
    private String behaviorName;
    /**
     * 玩家行为id
     */
    private Integer behaviorType;
    /**
     * 玩家当前行为的数值
     */
    private Long values;
}
