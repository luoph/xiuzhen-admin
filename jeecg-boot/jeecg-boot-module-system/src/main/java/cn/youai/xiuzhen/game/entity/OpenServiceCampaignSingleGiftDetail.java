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
 * @description 开服活动-单笔好礼活动参数
 * @date 2020-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_open_service_campaign_single_gift_detail")
public class OpenServiceCampaignSingleGiftDetail extends OpenServiceCampaignDetail {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignSingleGiftDetail() {
    }

    public OpenServiceCampaignSingleGiftDetail(OpenServiceCampaignSingleGiftDetail other) {
//        this.id = other.id;;
//        this.campaignId = other.campaignId;;
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
        this.sort = other.sort;
        this.emailTitle = other.emailTitle;
        this.emailContent = other.emailContent;
    }

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    /**
     * 邮件标题
     */
    @ExcelProperty("邮件标题")
    @Excel(name = "邮件标题", width = 15)
    private java.lang.String emailTitle;

    /**
     * 邮件描述
     */
    @ExcelProperty("邮件描述")
    @Excel(name = "邮件描述", width = 15)
    private java.lang.String emailContent;

    @TableField(exist = false)
    private List<OpenServiceCampaignSingleGiftItem> detailItemList;

    @TableField(exist = false)
    private List<OpenServiceCampaignSingleGiftNotice> messageList;

}
