package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码活动分组
 * @date 2020-01-07
 */
@Data
@TableName("game_redeem_activity_group")
@Accessors(chain = true)
public class GameRedeemActivityGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    private java.lang.String name;

    /**
     * 分组说明
     */
    @Excel(name = "分组说明", width = 15)
    private java.lang.String summary;

    /**
     * 限制次数, 0表示不限制
     */
    @Excel(name = "限制次数, 0表示不限制", width = 15)
    private java.lang.Integer limitCount;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;
}
