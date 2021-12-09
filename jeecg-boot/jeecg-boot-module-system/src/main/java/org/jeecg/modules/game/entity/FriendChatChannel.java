package org.jeecg.modules.game.entity;

import cn.youai.entities.GamePlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/21 16:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FriendChatChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 渠道id
     */
    private Long channelId;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 状态 0 - 无效 1 - 有效
     */
    private int status;

    /**
     * 聊天类型 1 - 私聊 2 - 群聊
     */
    private int type;

    /**
     * 玩家重置时间
     */
    private Date resetTime;

    /**
     * 玩家消息发送时间
     */
    private Date sendTime;

    /**
     * 玩家读取时间
     */
    private Date readTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 关联私聊消息对象
     */
    private FriendChatMessage friendChatMessage;

    /**
     * 关联玩家
     */
    private GamePlayer player;

}
