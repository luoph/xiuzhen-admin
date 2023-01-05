package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服互动消耗配置
 * @date 2020-12-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_consume_detail")
public class OpenServiceCampaignConsumeDetail extends OpenServiceCampaignDetail {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignConsumeDetail() {
    }

    public OpenServiceCampaignConsumeDetail(OpenServiceCampaignConsumeDetail other) {
//        this.id = other.id;
//        this.campaignId = other.campaignId;
//        this.campaignTypeId = other.campaignTypeId;
        super.setName(other.getName());
        super.setTabName(other.getTabName());
        super.setBanner(other.getBanner());
        super.setHelpMsg(other.getHelpMsg());
        super.setTimeType(other.getTimeType());
        super.setStartTime(other.getStartTime());
        super.setEndTime(other.getEndTime());
        super.setStartDay(other.getStartDay());
        super.setDuration(other.getDuration());
        this.consumeRewardEmailTitle = other.consumeRewardEmailTitle;
        this.consumeRewardEmailContent = other.consumeRewardEmailContent;
    }

    /**
     * 消耗奖励邮件标题
     */
    @ExcelProperty("消耗奖励邮件标题")
    @Excel(name = "消耗奖励邮件标题", width = 15)
    private java.lang.String consumeRewardEmailTitle;

    /**
     * 消耗奖励邮件内容
     */
    @ExcelProperty("消耗奖励邮件内容")
    @Excel(name = "消耗奖励邮件内容", width = 15)
    private java.lang.String consumeRewardEmailContent;

    @TableField(exist = false)
    private List<OpenServiceCampaignConsumeDetailItem> consumeList;

    @TableField(exist = false)
    private List<OpenServiceCampaignConsumeDetailMessage> messageList;

}
