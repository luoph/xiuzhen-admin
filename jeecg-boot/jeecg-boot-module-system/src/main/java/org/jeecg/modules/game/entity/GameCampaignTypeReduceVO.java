package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @date 2021-02-02
 */
@Data
public class GameCampaignTypeReduceVO {

    /**
     * 排名序列
     */
    @ExcelProperty("排名序列")
    private Integer sort;

    /**
     * 消耗道具id,item_id
     */
    @ExcelProperty("消耗道具id,item_id")
    private Integer reduceItemId;

    /**
     * 上榜下限数量
     */
    @ExcelProperty("上榜下限数量")
    private Integer limitNum;

    /**
     * 奖励内容[{"itemId":1001,"num":100}]
     */
    @ExcelProperty("奖励内容")
    private String reward;

}
