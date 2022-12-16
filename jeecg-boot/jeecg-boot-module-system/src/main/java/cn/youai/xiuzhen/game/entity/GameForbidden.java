package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @description game_forbidden
 * @date 2020-12-17
 */
@Data
@Accessors(chain = true)
@TableName("game_forbidden")
@EqualsAndHashCode(callSuper = true)
public class GameForbidden extends BaseEntity {

    private static final long serialVersionUID = 1L;

    GameForbidden() {
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.Integer serverId;

    /**
     * 1-登录 2-聊天
     */
    @Excel(name = "1-登录 2-聊天", width = 15)
    private java.lang.Integer type;

    /**
     * playerId/ip/deviceId
     */
    @Excel(name = "playerId/ip/deviceId", width = 15)
    private java.lang.String banKey;

    /**
     * 对应 ban_type 的值
     */
    @Excel(name = "对应 ban_type 的值", width = 15)
    private java.lang.String banValue;

    /**
     * 封禁原因
     */
    @Excel(name = "封禁原因", width = 15)
    private java.lang.String reason;

    /**
     * 0-临时 1-永久
     */
    @Excel(name = "0-临时 1-永久", width = 15)
    private java.lang.Integer isForever;

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
    private Integer duration;

    public void copy(GameForbidden other) {
        this.setStartTime(other.getStartTime());
        this.setEndTime(other.getEndTime());
        this.setIsForever(other.getIsForever());
        this.setReason(other.getReason());
    }
}
