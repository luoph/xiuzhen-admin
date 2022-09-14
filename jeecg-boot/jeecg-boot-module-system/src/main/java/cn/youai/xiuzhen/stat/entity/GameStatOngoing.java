/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.entity;

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
 * 30天留存和ltv统计
 * </p>
 *
 * @author buliangliang
 * @since 2020-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameStatOngoing implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 统计类型 1-留存  2-ltv
     */
    private Integer type;

    /**
     * 新增玩家
     */
    private Long registerNum;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 1留存或者ltv
     */
    private BigDecimal c1;

    /**
     * 2留存或者ltv
     */
    private BigDecimal c2;

    /**
     * 3留存或者ltv
     */
    private BigDecimal c3;

    /**
     * 4留存或者ltv
     */
    private BigDecimal c4;

    /**
     * 5留存或者ltv
     */
    private BigDecimal c5;

    /**
     * 6留存或者ltv
     */
    private BigDecimal c6;

    /**
     * 7留存或者ltv
     */
    private BigDecimal c7;

    /**
     * 8留存或者ltv
     */
    private BigDecimal c8;

    /**
     * 9留存或者ltv
     */
    private BigDecimal c9;

    /**
     * 10留存或者ltv
     */
    private BigDecimal c10;

    /**
     * 11留存或者ltv
     */
    private BigDecimal c11;

    /**
     * 12留存或者ltv
     */
    private BigDecimal c12;

    /**
     * 13留存或者ltv
     */
    private BigDecimal c13;

    /**
     * 14留存或者ltv
     */
    private BigDecimal c14;

    /**
     * 15留存或者ltv
     */
    private BigDecimal c15;

    /**
     * 16留存或者ltv
     */
    private BigDecimal c16;

    /**
     * 17留存或者ltv
     */
    private BigDecimal c17;

    /**
     * 18留存或者ltv
     */
    private BigDecimal c18;

    /**
     * 19留存或者ltv
     */
    private BigDecimal c19;

    /**
     * 20留存或者ltv
     */
    private BigDecimal c20;

    /**
     * 21留存或者ltv
     */
    private BigDecimal c21;

    /**
     * 22留存或者ltv
     */
    private BigDecimal c22;

    /**
     * 23留存或者ltv
     */
    private BigDecimal c23;

    /**
     * 24留存或者ltv
     */
    private BigDecimal c24;

    /**
     * 25留存或者ltv
     */
    private BigDecimal c25;

    /**
     * 26留存或者ltv
     */
    private BigDecimal c26;

    /**
     * 27留存或者ltv
     */
    private BigDecimal c27;

    /**
     * 28留存或者ltv
     */
    private BigDecimal c28;

    /**
     * 29留存或者ltv
     */
    private BigDecimal c29;

    /**
     * 30留存或者ltv
     */
    private BigDecimal c30;

    /**
     * 创建时间
     */
    private Date createTime;


}
