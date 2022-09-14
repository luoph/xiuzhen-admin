package cn.youai.xiuzhen.game.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/20 17:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserOnlineRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 账户
     */
    private String account;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 在线时长(秒)
     */
    private Long duration;

    /**
     * 在线时长(分)
     */
    private Long durationMinutes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建日期
     */
    private Date createDate;
}
