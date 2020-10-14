package org.jeecg.modules.game.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description daily_gift_package_buy
 * @date 2020-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DailyGiftPackageBuyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Integer id;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 礼包id
     */
    private Integer giftPackageId;

    /**
     * 购买日期
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date buyDate;

    /**
     * buyTimes
     */
    private Integer buyTimes;

    /**
     * 奖励物品
     */
    private String reward;

    /**
     * 充值金额
     */
    private java.math.BigDecimal rechargeAmount;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;

    /**
     * 消费数量（这件礼包的总数）
     */
    private Integer giftCount;

    /**
     * 该道具消费礼包次数占总数的比例
     */
    private BigDecimal giftCountRatio;

    /**
     * 该消费礼包的金额总数
     */
    private BigDecimal rechargeAmountSum;

    /**
     * 该件消费礼包的消费金额占总消费金额的比例
     */
    private BigDecimal rechargeAmountRatio;

}
