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
 * @description 剧情分析
 * @date 2021-03-26
 */
@Data
@TableName("game_story_analysis")
@Accessors(chain = true)
public class GameStoryAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分析日期
     */
    @Excel(name = "分析日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date analysisDate;

    /**
     * 剧情小关卡
     */
    @Excel(name = "剧情小关卡", width = 15)
    private java.lang.Integer storyCheckpoint;

    /**
     * 停留活跃人数
     */
    @Excel(name = "停留活跃人数", width = 15)
    private java.lang.Integer stayLiveNum;

    /**
     * 活跃占比
     */
    @Excel(name = "活跃占比", width = 15)
    private java.lang.Integer liveRate;

    /**
     * 停留流失人数
     */
    @Excel(name = "停留流失人数", width = 15)
    private java.lang.Integer stayLeaveNum;

    /**
     * 流失占比
     */
    @Excel(name = "流失占比", width = 15)
    private java.lang.Integer leaveRate;

    /**
     * 总达成人数
     */
    @Excel(name = "总达成人数", width = 15)
    private java.lang.Integer totalArriveNum;

    /**
     * 总滞留人数
     */
    @Excel(name = "总滞留人数", width = 15)
    private java.lang.Integer totalStayNum;

    /**
     * 关卡滞留率
     */
    @Excel(name = "关卡滞留率", width = 15)
    private java.lang.Integer checkpointStayRate;
}
