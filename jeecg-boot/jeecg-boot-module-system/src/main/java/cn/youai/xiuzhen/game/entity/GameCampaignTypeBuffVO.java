package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description Buff活动
 * @date 2020-10-15
 */
@Data
@Accessors(chain = true)
public class GameCampaignTypeBuffVO {

    /**
     * 活动项类型: 1.登录礼包, 2.累计充值, 3.兑换 4.节日任务, 5.buff-修为加成, 6.buff-灵气加成
     */
    @ExcelProperty("活动项类型")
    private Integer type;

    /**
     * 开始时间
     */
    @ExcelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty("结束时间")
    private Date endTime;

    /**
     * 描述
     */
    @ExcelProperty("描述")
    private String description;

    /**
     * 加成
     */
    @ExcelProperty("加成")
    private Integer addition;
}
