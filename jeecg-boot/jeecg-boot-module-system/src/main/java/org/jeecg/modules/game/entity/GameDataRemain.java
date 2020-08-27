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
 * 留存统计
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameDataRemain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 渠道名
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 新增角色数
     */
    private Integer registerNum;

    /**
     * 付费用户数
     */
    private Integer payNum;

    /**
     * 付费率
     */
    private BigDecimal payRate;

    /**
     * 首日免费角色
     */
    private Integer freeNum;

    /**
     * 付费角色次留
     */
    private Integer payRemain;

    /**
     * 免费角色次留
     */
    private Integer freeRemain;

    /**
     * 2日留存率
     */
    private BigDecimal d2Remain;

    /**
     * 3日留存率
     */
    private BigDecimal d3Remain;

    /**
     * 4日留存率
     */
    private BigDecimal d4Remain;

    /**
     * 5日留存率
     */
    private BigDecimal d5Remain;

    /**
     * 6日留存率
     */
    private BigDecimal d6Remain;

    /**
     * 7日留存率
     */
    private BigDecimal d7Remain;

    /**
     * 15日留存率
     */
    private BigDecimal d15Remain;

    /**
     * 30日留存率
     */
    private BigDecimal d30Remain;

    /**
     * 60日留存率
     */
    private BigDecimal d60Remain;

    /**
     * 90日留存率
     */
    private BigDecimal d90Remain;

    /**
     * 120日留存率
     */
    private BigDecimal d120Remain;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 免费呲溜率
     */
    private BigDecimal freeRemainRate;

    /**
     * 付费次留率
     */
    private BigDecimal payRemainRate;

}
