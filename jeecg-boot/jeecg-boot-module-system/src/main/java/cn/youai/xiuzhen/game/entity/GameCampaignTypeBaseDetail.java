package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 任务活动
 * @date 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeBaseDetail extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;
    
    /**
     * 最小世界等级
     */
    @ExcelProperty("最小世界等级")
    @Excel(name = "最小世界等级", width = 15)
    private Integer minLevel;

    /**
     * 最大世界等级
     */
    @ExcelProperty("最大世界等级")
    @Excel(name = "最大世界等级", width = 15)
    private Integer maxLevel;
}
