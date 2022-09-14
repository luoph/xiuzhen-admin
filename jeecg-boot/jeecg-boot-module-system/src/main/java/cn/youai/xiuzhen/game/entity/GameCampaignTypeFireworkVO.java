package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日烟花
 * @date 2021-01-15
 */
@Data
@Accessors(chain = true)
public class GameCampaignTypeFireworkVO {

    /**
     * 礼包itemId
     */
    @ExcelProperty("礼包Id")
    private Integer giftId;

    /**
     * 购买次数 =1-单次购买 >1-多次购买
     */
    @ExcelProperty("购买类型")
    private Integer times;

    /**
     * 价格
     */
    @ExcelProperty("价格")
    private String price;

    /**
     * 折扣
     */
    @ExcelProperty("折扣")
    private Integer discount;

    /**
     * 单次购买数量
     */
    @ExcelProperty("单次购买数量")
    private Integer num;

    /**
     * 按钮标题
     */
    @ExcelProperty("按钮标题")
    private String btnName;

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
