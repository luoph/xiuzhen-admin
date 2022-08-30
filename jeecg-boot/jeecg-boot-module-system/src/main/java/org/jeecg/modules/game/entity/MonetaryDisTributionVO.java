package org.jeecg.modules.game.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author huli
 * @Description: MonetaryDisTributionVO
 * @date 2020/12/29 10:51
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MonetaryDisTributionVO implements Serializable {
    private String id;
    /**
     * 产销点
     */
    private String productAndMarket;
    /**
     * 货币数量
     */
    private Long quantityOfMoney;
    /**
     * 人数
     */
    private Integer numberOfPeople;
    /**
     * 次数
     */
    private Integer times;
    /**
     * 占比
     */
    private BigDecimal proportion;
}