/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 接口调用超时统计
 * </p>
 *
 * @author luopeihuan
 * @since 2023-07-11
 */
@Data
@Accessors(chain = true)
public class GameStatCmd implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 消息id
     */
    private Integer msgId;

    /**
     * 消息名
     */
    @TableField(exist = false)
    private String msgName;

    /**
     * 统计次数
     */
    private Integer num;

    /**
     * 接口耗时（秒）
     */
    private Integer costTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建日期
     */
    private Date createDate;


}
