package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@TableName("game_campaign_type_buff")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeBuff extends GameCampaignTypeBase {

    private static final long serialVersionUID = 1L;

    /**
     * 活动项类型: 1.登录礼包, 2.累计充值, 3.兑换 4.节日任务, 5.buff-修为加成, 6.buff-灵气加成
     */
    @Excel(name = "活动项类型", width = 15)
    private Integer type;

    /**
     * buff id
     */
    @Excel(name = "buff id", width = 15)
    private Integer buffId;

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
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private String description;

    /**
     * 加成
     */
    @Excel(name = "加成", width = 15)
    private Integer addition;
}
