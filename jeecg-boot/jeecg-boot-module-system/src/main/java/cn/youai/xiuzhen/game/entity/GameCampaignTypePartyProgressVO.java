package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日派对进度任务
 * @date 2021-03-30
 */
@Data
public class GameCampaignTypePartyProgressVO {

    /**
     * 任务规定数量
     */
    @ExcelProperty("任务规定数量")
    private Integer target;

    /**
     * 进度百分比
     */
    @ExcelProperty("进度百分比")
    private Integer percent;

    /**
     * 任务奖励
     */
    @ExcelProperty("任务奖励")
    private String reward;

}
