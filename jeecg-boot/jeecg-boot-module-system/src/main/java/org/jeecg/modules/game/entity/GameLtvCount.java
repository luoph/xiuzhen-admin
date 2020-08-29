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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * LTV统计
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameLtvCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String channel;

    private Integer serverId;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 注册角色数
     */
    private Integer registerNum;

    /**
     * 第1天充值
     */
    private Integer d1Amount;

    /**
     * 第2天充值
     */
    private Integer d2Amount;

    /**
     * 第3天充值
     */
    private Integer d3Amount;

    /**
     * 第4天充值
     */
    private Integer d4Amount;

    /**
     * 第5天充值
     */
    private Integer d5Amount;

    /**
     * 第6天充值
     */
    private Integer d6Amount;

    /**
     * 第7天充值
     */
    private Integer d7Amount;

    /**
     * 第8天充值
     */
    private Integer d14Amount;

    /**
     * 第9天充值
     */
    private Integer d21Amount;

    /**
     * 第10天充值
     */
    private Integer d30Amount;

    /**
     * 第11天充值
     */
    private Integer d60Amount;

    /**
     * 第12天充值
     */
    private Integer d90Amount;

    /**
     * 第13天充值
     */
    private Integer d120Amount;


    /**
     * 创建时间
     */
    private Date createTime;


}
