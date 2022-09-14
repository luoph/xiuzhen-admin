package cn.youai.xiuzhen.stat.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameChalcedonyOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private java.lang.Integer id;
    /**
     * 时间段日期展示
     */
    private String rangeTimeDate;

    /**
     * 消耗档位
     */
    private BigDecimal consumeRank;
    /**
     * 当天DAU
     */
    private Long dau;

    /**
     * 购买人数
     */
    private Integer payNum;
    /**
     * 购买次数
     */
    private Integer payCount;

    /**
     * 礼包购买率
     */
    private String payNumRate;

    /**
     * 消耗玉髓
     */
    private BigDecimal chalcedony;

}
