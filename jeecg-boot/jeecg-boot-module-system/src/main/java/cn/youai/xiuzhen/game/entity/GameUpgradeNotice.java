package cn.youai.xiuzhen.game.entity;

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
 * @description 更新公告
 * @date 2021-03-02
 */
@Data
@TableName("game_upgrade_notice")
@Accessors(chain = true)
public class GameUpgradeNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 标题
     */
    @Excel(name = "标题", width = 15)
    private java.lang.String title;

    /**
     * 正文
     */
    @Excel(name = "正文", width = 15)
    private java.lang.String noticeMsg;

    /**
     * 奖励
     */
    @Excel(name = "奖励", width = 15)
    private java.lang.String reward;

    /**
     * 已刷新的服务器id，使用,分割
     */
    @Excel(name = "已刷新的服务器id", width = 15)
    private java.lang.String lastServerIds;

    /**
     * 服务器
     */
    @Excel(name = "服务器", width = 15)
    private java.lang.String serverIds;

    /**
     * 状态0-关闭
     */
    @Excel(name = "状态0-关闭", width = 15)
    private java.lang.Integer status;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date endTime;

    /**
     * createTime
     */
    @Excel(name = "createTime", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * createBy
     */
    @Excel(name = "createBy", width = 15)
    private java.lang.String createBy;

    /**
     * updateTime
     */
    @Excel(name = "updateTime", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date updateTime;

    /**
     * updateBy
     */
    @Excel(name = "updateBy", width = 15)
    private java.lang.String updateBy;
}
