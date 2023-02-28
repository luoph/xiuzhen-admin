package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 问卷调查
 * @date 2021-01-13
 */
@Data
@TableName("game_questionnaire")
@EqualsAndHashCode(callSuper = true)
public class GameQuestionnaire extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Excel(name = "id", width = 15)
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 服务器id，使用,分割
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.String serverIds;

    /**
     * 已刷新的服务器id，使用,分割
     */
    @Excel(name = "已刷新的服务器id", width = 15)
    private java.lang.String lastServerIds;

    /**
     * 问卷调查地址
     */
    @Excel(name = "问卷调查地址", width = 15)
    private java.lang.String url;

    /**
     * 状态: 0.关闭, 1.开启
     */
    @Excel(name = "状态", width = 15)
    private java.lang.Integer status;

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

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;
}
