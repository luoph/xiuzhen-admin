package cn.youai.xiuzhen.game.entity;

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
public class ChatMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 聊天频道
     */
    private String chatChannel;

    /**
     * 聊天频道
     */
    private Integer serverId;

    /**
     * 发送方玩家id
     */
    private Long sendPlayerId;

    /**
     * 发送方玩家名字
     */
    private String sendPlayerName;

    /**
     * 接收方玩家id
     */
    private Long receivePlayerId;

    /**
     * 接收方玩家名字
     */
    private String receivePlayerName;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息发送时间
     */
    private Date messageTime;

}
