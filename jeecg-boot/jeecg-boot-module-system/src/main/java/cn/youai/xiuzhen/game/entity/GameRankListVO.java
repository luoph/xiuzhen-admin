package cn.youai.xiuzhen.game.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 排行榜   游戏后台-数据统计-玩家数据处新增排行模块
 *
 * @author huli
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameRankListVO {
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 服务器id
     */
    private Integer serverId;
    /**
     * 查询的年月日
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date date;
    /**
     * 查询的小时
     */
    private Integer hour;
    /**
     * 榜单类型
     */
    private Integer rankListType;

    /**
     * 排名
     */
    private Integer ranking;
    /**
     * 玩家id
     */
    private Long playerId;
    /**
     * 玩家名
     */
    private String playerName;
    /**
     * 境界
     */
    private String realm;
    /**
     * 榜单数值
     */
    private Long rankValues;

}
