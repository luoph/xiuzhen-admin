package cn.youai.xiuzhen.stat.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */

@Data
@Accessors(chain = true)
public class CombatPowerLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String channel;

    /**
     * 玩家id
     */
    @Excel(name = "游戏服id", width = 15)
    private Integer serverId;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    /**
     * 玩家名
     */
    @Excel(name = "玩家名", width = 15)
    private String nickname;

    /**
     * 模块id
     */
    @Excel(name = "模块id", width = 15)
    private Integer attrType;

    /**
     * 属性模块
     */
    @Excel(name = "属性模块", width = 15)
    private String attrName;

    /**
     * 战力变更
     */
    @Excel(name = "战力变更", width = 15)
    private Long delta;

    /**
     * 原战力
     */
    @Excel(name = "原战力", width = 15)
    private Long before;

    /**
     * 新战力
     */
    @Excel(name = "新战力", width = 15)
    private Long after;

    /**
     * 时间
     */
    @Excel(name = "时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date createTime;
}