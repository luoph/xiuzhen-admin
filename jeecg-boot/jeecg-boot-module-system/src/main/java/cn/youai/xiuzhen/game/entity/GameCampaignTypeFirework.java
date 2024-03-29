package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 节日烟花
 * @date 2021-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_firework")
public class GameCampaignTypeFirework extends GameCampaignTypeBaseDetail {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeFirework() {
    }

    public GameCampaignTypeFirework(GameCampaignTypeFirework other) {
        this.giftId = other.getGiftId();
        this.times = other.getTimes();
        this.price = other.getPrice();
        this.discount = other.getDiscount();
        this.num = other.getNum();
        this.btnName = other.getBtnName();
        this.setMinLevel(other.getMinLevel());
        this.setMaxLevel(other.getMaxLevel());
    }

    /**
     * 礼包itemId
     */
    @Excel(name = "礼包id", width = 15)
    private java.lang.Integer giftId;

    /**
     * 购买次数 =1-单次购买 >1-多次购买
     */
    @Excel(name = "购买类型", width = 15)
    private java.lang.Integer times;

    /**
     * 价格
     */
    @Excel(name = "价格", width = 15)
    private java.lang.String price;

    /**
     * 折扣
     */
    @Excel(name = "折扣", width = 15)
    private java.lang.Integer discount;

    /**
     * 单次购买数量
     */
    @Excel(name = "单次购买数量", width = 15)
    private java.lang.Integer num;

    /**
     * 按钮标题
     */
    @Excel(name = "按钮标题", width = 15)
    private java.lang.String btnName;
}
