package org.jeecg.modules.player.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/23 17:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerBehavior implements Serializable {

    private static final long serialVersionUID = 1L;
    @ExcelIgnore
    private Long id;

    /**
     * 服务器id
     */
    @ColumnWidth(15)
    @ExcelProperty("服务器id")
    @Excel(name = "服务器id", width = 15)
    private Integer serverId;

    /**
     * 玩家id
     */
    @ColumnWidth(15)
    @ExcelProperty("玩家id")
    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    /**
     * 玩家相关信息分类类型
     */
    @ExcelIgnore
    private Integer type;

    /**
     * type对应的值
     */
    @ExcelIgnore
    private Long value;

    /**
     * 玩家名
     */
    @ColumnWidth(15)
    @ExcelProperty("玩家名")
    @Excel(name = "玩家名", width = 15)
    private String nickname;


    /**
     * 修炼年数
     */
    @ColumnWidth(15)
    @ExcelProperty("修炼年数")
    @Excel(name = "修炼年数", width = 15)
    private Long practiceYear;

    /**
     * 境界等级
     */
    @ColumnWidth(15)
    @ExcelProperty("境界等级")
    @Excel(name = "境界等级", width = 15)
    private Long reamLevel;

    /**
     * 斗法次数
     */
    @ColumnWidth(15)
    @ExcelProperty("福地夺宝/斗法-挑战")
    @Excel(name = "福地夺宝/斗法-挑战", width = 15)
    private Long arenaBattle;

    /**
     * 仙兽秘境
     */
    @ColumnWidth(15)
    @ExcelProperty("仙兽秘境")
    @Excel(name = "仙兽秘境", width = 15)
    private Long mapExplore;

    /**
     * 剧情关数
     */
    @ColumnWidth(15)
    @ExcelProperty("剧情关数")
    @Excel(name = "剧情关数", width = 15)
    private Long mainStoryLevel;

    /**
     * 灵界器灵
     */
    @ColumnWidth(15)
    @ExcelProperty("灵界器灵")
    @Excel(name = "灵界器灵", width = 15)
    private Long spiritualWorldBoss;

    /**
     * 魔王入侵
     */
    @ColumnWidth(15)
    @ExcelProperty("魔王入侵")
    @Excel(name = "魔王入侵", width = 15)
    private Long worldBoss;

    /**
     * 仙盟仙宴
     */
    @ColumnWidth(15)
    @ExcelProperty("仙盟仙宴")
    @Excel(name = "仙盟仙宴", width = 15)
    private Long factionBanquet;

    /**
     * 当天充值
     */
    @ColumnWidth(15)
    @ExcelProperty("当天充值")
    @Excel(name = "当天充值", width = 15)
    private Double recharge;

    /**
     * 玉髓消耗
     */
    @ColumnWidth(15)
    @ExcelProperty("玉髓消耗")
    @Excel(name = "玉髓消耗", width = 15)
    private Long consumeMoney;

    /**
     * 阅历值
     */
    @ColumnWidth(15)
    @ExcelProperty("阅历值")
    @Excel(name = "阅历值", width = 15)
    private Long experience;

    /**
     * 在线时长
     */
    @ColumnWidth(15)
    @ExcelProperty("在线时长")
    @Excel(name = "在线时长", width = 15)
    private Long onlineTime;
    
    /**
     * 上古遗迹
     */
    @ColumnWidth(15)
    @ExcelProperty("上古遗迹")
    @Excel(name = "上古遗迹", width = 15)
    private Long travelHill;

    /**
     * 北冥魔海
     */
    @ColumnWidth(15)
    @ExcelProperty("北冥魔海")
    @Excel(name = "北冥魔海", width = 15)
    private Long mainStoryBoss;

    /**
     * 不死魔巢
     */
    @ColumnWidth(15)
    @ExcelProperty("不死魔巢")
    @Excel(name = "不死魔巢", width = 15)
    private Long petBoss;

    /**
     * 神游次数
     */
    @ColumnWidth(15)
    @ExcelProperty("神游次数")
    @Excel(name = "神游次数", width = 15)
    private Long travelTimes;

    /**
     * 玩家等级
     */
    @ColumnWidth(15)
    @ExcelProperty("玩家等级")
    @Excel(name = "玩家等级", width = 15)
    private Long playerLevel;

    /**
     * 战力
     */
    @ColumnWidth(15)
    @ExcelProperty("战力")
    @Excel(name = "战力", width = 15)
    private Long combatPower;

    /**
     * 冒险等级
     */
    @ColumnWidth(15)
    @ExcelProperty("冒险等级")
    @Excel(name = "冒险等级", width = 15)
    private Long mapLevel;

    /**
     * 符文秘境-小灵山
     */
    @ColumnWidth(15)
    @ExcelProperty("符文秘境-小灵山")
    @Excel(name = "符文秘境-小灵山", width = 15)
    private Long lingShanLevel;

    /**
     * 剧情小关卡
     */
    @ColumnWidth(15)
    @ExcelProperty("剧情小关卡")
    @Excel(name = "剧情小关卡", width = 15)
    private Long mainStoryCheck;

    /**
     * 神魔战场
     */
    @ColumnWidth(15)
    @ExcelProperty("神魔战场")
    @Excel(name = "神魔战场", width = 15)
    private Long ghostWar;

    /**
     * 丹药秘境
     */
    @ColumnWidth(15)
    @ExcelProperty("丹药秘境")
    @Excel(name = "丹药秘境", width = 15)
    private Long godRoad;
    /**
     * 仙器秘境
     */
    @ColumnWidth(15)
    @ExcelProperty("仙器秘境")
    @Excel(name = "仙器秘境", width = 15)
    private Long tierMapExplore;
    /**
     * 仙缘双休
     */
    @ColumnWidth(15)
    @ExcelProperty("仙缘双休")
    @Excel(name = "仙缘双休", width = 15)
    private Long matePractice;
    /**
     * 仙缘试炼
     */
    @ColumnWidth(15)
    @ExcelProperty("仙缘试炼")
    @Excel(name = "仙缘试炼", width = 15)
    private Long mateBoss;
    /**
     * 日期
     */
    @ColumnWidth(15)
    @ExcelProperty("统计日期")
    @Excel(name = "统计日期", width = 15)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date createDate;


}
