package cn.youai.xiuzhen.stat.entity;

import cn.youai.entities.GamePlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/20 21:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FriendChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 会话渠道id
     */
    private Long channelId;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 私聊内容
     */
    private String message;

    /**
     * 消息分发频道
     */
    private Integer messageChannel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 玩家
     */
    private GamePlayer player;

    private FriendChatChannel friendChatChannel;
}
