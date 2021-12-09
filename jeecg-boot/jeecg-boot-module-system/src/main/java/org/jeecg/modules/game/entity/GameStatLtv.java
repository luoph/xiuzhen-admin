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
public class GameStatLtv implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    private BigDecimal d1Amount;

    /**
     * 第2天充值
     */
    private BigDecimal d2Amount;

    /**
     * 第3天充值
     */
    private BigDecimal d3Amount;

    /**
     * 第4天充值
     */
    private BigDecimal d4Amount;

    /**
     * 第5天充值
     */
    private BigDecimal d5Amount;

    /**
     * 第6天充值
     */
    private BigDecimal d6Amount;

    /**
     * 第7天充值
     */
    private BigDecimal d7Amount;

    /**
     * 第14天充值
     */
    private BigDecimal d14Amount;

    /**
     * 第21天充值
     */
    private BigDecimal d21Amount;

    /**
     * 第30天充值
     */
    private BigDecimal d30Amount;

    /**
     * 第60天充值
     */
    private BigDecimal d60Amount;

    /**
     * 第90天充值
     */
    private BigDecimal d90Amount;

    /**
     * 第120天充值
     */
    private BigDecimal d120Amount;

    /**
     * 第180天充值
     */
    private BigDecimal d180Amount;

    /**
     * 第360天充值
     */
    private BigDecimal d360Amount;

    /**
     * 创建时间
     */
    private Date createTime;
}
