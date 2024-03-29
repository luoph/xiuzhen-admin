package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description Buff活动
 * @date 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_buff")
public class GameCampaignTypeBuff extends GameCampaignTypeBaseDetail {

    private static final long serialVersionUID = 1L;

    public GameCampaignTypeBuff() {

    }

    public GameCampaignTypeBuff(GameCampaignTypeBuff other) {
        this.type = other.getType();
        this.startTime = other.getStartTime();
        this.endTime = other.getEndTime();
        this.description = other.getDescription();
        this.addition = other.getAddition();
        this.setMinLevel(other.getMinLevel());
        this.setMaxLevel(other.getMaxLevel());
    }

    /**
     * 活动项类型: 1.登录礼包, 2.累计充值, 3.兑换 4.节日任务, 5.buff-修为加成, 6.buff-灵气加成
     */
    @ExcelProperty("活动项类型")
    @Excel(name = "活动项类型", width = 15)
    private Integer type;

    /**
     * 开始时间
     */
    @ExcelProperty("开始时间")
    @Excel(name = "开始时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty("结束时间")
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date endTime;

    /**
     * 描述
     */
    @ExcelProperty("描述")
    @Excel(name = "描述", width = 15)
    private String description;

    /**
     * 加成
     */
    @ExcelProperty("加成")
    @Excel(name = "加成", width = 15)
    private Integer addition;
}
