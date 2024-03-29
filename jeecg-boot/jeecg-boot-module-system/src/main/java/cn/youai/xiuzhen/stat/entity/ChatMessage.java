package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.game.entity.GamePlayer;
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
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 渠道
     */
    private Integer channel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 玩家
     */
    private GamePlayer player;
}
