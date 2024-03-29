package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动类型配置
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign_type")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public GameCampaignType() {

    }

    public GameCampaignType(GameCampaignType other) {
        this.name = other.name;
        this.type = other.type;
        this.typeImage = other.typeImage;
        this.sort = other.sort;
        this.extra = other.extra;
        this.animation = other.animation;
        this.resType = other.resType;
        this.cross = other.cross;
        this.timeType = other.timeType;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.startDay = other.startDay;
        this.duration = other.duration;
        this.eggsIntegralGoods = other.eggsIntegralGoods;
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * campaign.id, 活动id
     */
    @Excel(name = "活动id", width = 15)
    private Long campaignId;

    /**
     * 活动页签名
     */
    @Excel(name = "活动页签名", width = 15)
    private String name;

    /**
     * 活动项类型: 1.登录礼包, 2.累计充值, 3.兑换, 4.节日任务, 5.buff-修为加成, 6.buff-灵气加成, 7-掉落, 8-烟花
     */
    @Excel(name = "活动项类型", width = 15)
    private Integer type;

    /**
     * 活动类型图片
     */
    @Excel(name = "活动类型图片", width = 15)
    private String typeImage;

    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    private Integer sort;

    /**
     * 额外参数
     */
    @Excel(name = "额外参数", width = 15)
    private String extra;

    /**
     * 动画
     */
    @Excel(name = "动画", width = 15)
    private String animation;


    /**
     * 资源格式: 1.骨骼, 2.序列帧, 3.图片
     */
    @Excel(name = "资源格式", width = 15)
    private Integer resType;

    /**
     * 活动类型-11 所需的商品列表
     */
    @Excel(name = "砸蛋商品", width = 15)
    private String eggsIntegralGoods;

    /**
     * 是否跨服
     */
    @Excel(name = "是否跨服", width = 15)
    @TableField(value = "`cross`")
    private Integer cross;

    /**
     * 时间类型: 1.具体时间范围, 2.开服第N天
     */
    @Excel(name = "时间类型", width = 15)
    private Integer timeType;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date endTime;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @Excel(name = "开始天数", width = 15)
    private Integer startDay;

    /**
     * 持续时间(天)
     */
    @Excel(name = "持续天数", width = 15)
    private Integer duration;

    /**
     * 页签详细配置
     * 1.登录礼包 - GameCampaignTypeLogin
     * 2.累计充值 - GameCampaignTypeRecharge
     * 3.兑换 - GameCampaignTypeExchange
     * 4.节日任务 - GameCampaignTypeTask
     * 5.buff-修为加成 - GameCampaignTypeBuff
     * 6.buff-灵气加成 - GameCampaignTypeBuff
     */
    @TableField(exist = false)
    private List<? extends GameCampaignTypeBase> details;

    /**
     * 节日掉落奖励配置
     */
    @TableField(exist = false)
    private List<? extends GameCampaignTypeBase> rewardList;

    /**
     * 页面更新过来的数据
     */
    @TableField(exist = false)
    private String detailsData;

    public boolean invalidTime() {
        return null != this.startTime && null != this.endTime && this.startTime.after(this.endTime);
    }
}
