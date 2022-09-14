/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 背包道具操作日志记录
 * </p>
 *
 * @author buliangliang
 * @since 2020-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BackpackLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long playerId;

    private Integer itemId;

    private Long num;

    /**
     * 1存入 2消耗
     */
    private Integer type;

    /**
     * 获取途径
     */
    private Integer way;

    /**
     * 变化前
     */
    private Long beforeNum;

    /**
     * 变化后
     */
    private Long afterNum;

    private Date createDate;

    private Date createTime;

}
