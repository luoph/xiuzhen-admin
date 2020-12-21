package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author buliangliang
 * @version V1.0
 * @description 游戏下发邮件
 * @date 2020-01-04
 */
@Data
@TableName("game_email")
@Accessors(chain = true)
public class GameEmail extends BaseEntity {

    /**
     * 主id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 标题
     */
    @Excel(name = "标题", width = 15)
    private java.lang.String title;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private java.lang.String remark;

    /**
     * 类型
     */
    @Excel(name = "类型", width = 15)
    private java.lang.Integer emailType;

    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    private java.lang.String content;

    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    private java.lang.Integer validState;

    /**
     * 目标类型
     */
    @Excel(name = "目标类型", width = 15)
    private java.lang.Integer targetType;

    /**
     * 目标主体::服务器id/玩家id
     */
    @Excel(name = "目标主体", width = 15)
    private java.lang.String targetIds;

    /**
     * 生效时间
     */
    @Excel(name = "生效时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date sendTime;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date endTime;

    @TableField(exist = false)
    private Long targetBodyId;
}
