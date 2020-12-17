package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值活动
 * @date 2020-10-15
 */
@Data
public class GameCampaignTypeRechargeVO {

    /**
     * 礼包id
     */
    @ExcelProperty("礼包id")
    private Integer rechargeId;

    /**
     * 累计充值额度
     */
    @ExcelProperty("累计充值额度")
    private BigDecimal rechargeAmount;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    private String reward;
}
