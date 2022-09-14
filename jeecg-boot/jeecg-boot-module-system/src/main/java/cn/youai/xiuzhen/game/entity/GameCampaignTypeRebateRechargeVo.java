package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 狂欢返利
 * @date 2021-05-31
 */
@Data
public class GameCampaignTypeRebateRechargeVo {

    /**
     * 礼包id
     */
    @ExcelProperty("礼包id")
    private Integer rid;

    /**
     * 累计充值天数
     */
    @ExcelProperty("累计充值天数")
    private Integer totalDay;

    /**
     * 最低充值金额
     */
    @ExcelProperty("最低充值金额")
    private java.math.BigDecimal min;

    /**
     * 进度描述
     */
    @ExcelProperty("进度描述")
    private String progressDesc;

    /**
     * 奖励列表
     */
    @ExcelProperty("奖励列表")
    private String reward;

    /**
     * 最小世界等级
     */
    @ExcelProperty("最小世界等级")
    private Integer minLevel;

    /**
     * 最大世界等级
     */
    @ExcelProperty("最大世界等级")
    private Integer maxLevel;
}
