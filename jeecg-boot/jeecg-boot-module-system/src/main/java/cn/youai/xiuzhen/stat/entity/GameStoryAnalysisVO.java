package cn.youai.xiuzhen.stat.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author lijunchi
 * @version V1.0
 * @description 剧情分析VO
 * @date 2021-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GameStoryAnalysisVO extends GameStoryAnalysis implements Serializable {

    private static final long serialVersionUID = -1960866186259048998L;

    public GameStoryAnalysisVO() {
    }

    public GameStoryAnalysisVO(Integer minorLevel, String chapterName) {
        super();
        setMinorLevel(minorLevel);
        setStoryCheckpoint(chapterName);
    }

    /**
     * 渠道
     */
    @ExcelIgnore
    @TableField(exist = false)
    private java.lang.String channelId;

    /**
     * 服务器id
     */
    @ExcelIgnore
    @TableField(exist = false)
    private java.lang.Integer serverId;

    /**
     * 分析日期
     */
    @ExcelIgnore
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date analysisDate;

    /**
     * 剧情小关卡
     */
    @Excel(name = "剧情小关卡", width = 15)
    private java.lang.String storyCheckpoint;

    /**
     * 停留活跃人数
     */
    @Excel(name = "停留活跃人数", width = 15)
    private java.lang.Integer stayLiveNum;

    /**
     * 活跃占比
     */
    @Excel(name = "活跃占比", width = 15)
    private java.lang.String liveRate;

    /**
     * 停留流失人数
     */
    @Excel(name = "停留流失人数", width = 15)
    private java.lang.Integer stayLeaveNum;

    /**
     * 流失占比
     */
    @Excel(name = "流失占比", width = 15)
    private java.lang.String leaveRate;

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
    private java.lang.String checkpointStayRate;
}
