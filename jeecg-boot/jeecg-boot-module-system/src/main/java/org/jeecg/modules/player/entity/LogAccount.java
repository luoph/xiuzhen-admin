/*
* create by mybatis-plus-generator  https://github.com/xiweile
*/
package org.jeecg.modules.player.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 玩家登录、创角统计
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    private String account;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 1-创角、2-登录
     */
    private Integer type;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建日期
     */
    private Date createDate;


}
