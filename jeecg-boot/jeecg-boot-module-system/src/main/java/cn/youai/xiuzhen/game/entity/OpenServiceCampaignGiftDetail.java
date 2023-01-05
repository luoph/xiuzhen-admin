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
 * @description 开服活动-开服排行-活动明细(3级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_gift_detail")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignGiftDetail extends OpenServiceCampaignDetail {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignGiftDetail() {
    }

    public OpenServiceCampaignGiftDetail(OpenServiceCampaignGiftDetail other) {
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
        this.resType = other.resType;
        this.skeleton = other.skeleton;
        this.sort = other.sort;
    }

    /**
     * 资源格式: 1.骨骼, 2.序列帧, 3.图片
     */
    @ExcelProperty("资源类型")
    @Excel(name = "资源类型", width = 15)
    private java.lang.String resType;
    /**
     * 骨骼动画资源
     */
    @ExcelProperty("骨骼动画资源")
    @Excel(name = "骨骼动画资源", width = 15)
    private java.lang.String skeleton;

    /**
     * 排序
     */
    @ExcelProperty("排序")
    @Excel(name = "排序", width = 15)
    private java.lang.Integer sort;

    @TableField(exist = false)
    private List<OpenServiceCampaignGiftDetailItem> detailItemList;

}
