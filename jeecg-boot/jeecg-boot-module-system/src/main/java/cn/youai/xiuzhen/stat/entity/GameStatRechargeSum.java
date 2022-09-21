package cn.youai.xiuzhen.stat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直充道具汇总
 * @date 2020-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameStatRechargeSum implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消费次数
     */
    private Integer totalNum;

    /**
     * 消费玩家数
     */
    private Integer totalPlayerNum;

    /**
     * 消费金额
     */
    private BigDecimal totalAmount;

}
