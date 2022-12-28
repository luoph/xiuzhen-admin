/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * <p>
 * 节日活动-自选特惠-传闻部分
 * </p>
 *
 * @author chuantian
 * @since 2021-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("game_campaign_type_select_discount_message")
public class GameCampaignTypeSelectDiscountMessage extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeSelectDiscountMessage() {
    }

    public GameCampaignTypeSelectDiscountMessage(GameCampaignTypeSelectDiscountMessage other) {
        this.pushTime = other.pushTime;
        this.content = other.content;
        this.num = other.num;
        this.emailTitle = other.emailTitle;
        this.emailContent = other.emailContent;
    }

    /**
     * 推送时间 时分秒
     */
    @Excel(name = "推送时间", width = 15)
    private String pushTime;

    /**
     * 传闻内容
     */
    @Excel(name = "传闻内容", width = 15)
    private String content;

    /**
     * 传闻广播次数
     */
    @Excel(name = "传闻广播次数", width = 15)
    private Integer num;

    /**
     * 全服广播邮件标题
     */
    @Excel(name = "全服广播邮件标题", width = 15)
    private String emailTitle;

    /**
     * 全服广播邮件内容
     */
    @Excel(name = "全服广播邮件内容", width = 15)
    private String emailContent;

}
