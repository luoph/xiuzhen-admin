package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author buliangliang
 * @version V1.0
 * @description 游戏下发邮件
 * @date 2020-01-04
 */
@Data
@TableName("game_email")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameEmail extends BaseEntity {

    /**
     * 主id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 标题
     */
    @Excel(name = "标题", width = 15)
    private java.lang.String title;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @TableField(value = "`describe`")
    private java.lang.String describe;

    /**
     * 类型
     */
    @Excel(name = "类型", width = 15)
    private java.lang.Integer type;

    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    private java.lang.String content;

    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    private java.lang.Integer state;

    /**
     * 目标类型
     */
    @Excel(name = "目标类型", width = 15)
    private java.lang.Integer receiverType;

    /**
     * 目标主体
     * 服务器id/玩家id
     */
    @Excel(name = "目标主体", width = 15)
    private java.lang.String receiverIds;

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
     * 审核人员
     */
    @ApiModelProperty(value = "审核人员")
    @Excel(name = "审核人员", width = 15)
    private String reviewBy;

    /**
     * 审核时间
     */
    @Excel(name = "审核时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date reviewTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date endTime;

    @TableField(exist = false)
    private Long receiverId;
}
