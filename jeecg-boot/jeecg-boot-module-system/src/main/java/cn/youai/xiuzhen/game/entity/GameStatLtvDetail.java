/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
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
 * @since 2021-12-22
 */
@Data
@Accessors(chain = true)
public class GameStatLtvDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 渠道
     */
    private Integer channel;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 新增用户数
     */
    private Integer num;

    /**
     * 首日金额
     */
    private BigDecimal d1;

    /**
     * 2日金额
     */
    private BigDecimal d2;

    /**
     * 3日金额
     */
    private BigDecimal d3;

    /**
     * 4日金额
     */
    private BigDecimal d4;

    /**
     * 5日金额
     */
    private BigDecimal d5;

    /**
     * 6日金额
     */
    private BigDecimal d6;

    /**
     * 7日金额
     */
    private BigDecimal d7;

    /**
     * 8日金额
     */
    private BigDecimal d8;

    /**
     * 9日金额
     */
    private BigDecimal d9;

    /**
     * 10日金额
     */
    private BigDecimal d10;

    /**
     * 11日金额
     */
    private BigDecimal d11;

    /**
     * 12日金额
     */
    private BigDecimal d12;

    /**
     * 13日金额
     */
    private BigDecimal d13;

    /**
     * 14日金额
     */
    private BigDecimal d14;

    /**
     * 15日金额
     */
    private BigDecimal d15;

    /**
     * 16日金额
     */
    private BigDecimal d16;

    /**
     * 17日金额
     */
    private BigDecimal d17;

    /**
     * 18日金额
     */
    private BigDecimal d18;

    /**
     * 19日金额
     */
    private BigDecimal d19;

    /**
     * 20日金额
     */
    private BigDecimal d20;

    /**
     * 21日金额
     */
    private BigDecimal d21;

    /**
     * 22日金额
     */
    private BigDecimal d22;

    /**
     * 23日金额
     */
    private BigDecimal d23;

    /**
     * 24日金额
     */
    private BigDecimal d24;

    /**
     * 25日金额
     */
    private BigDecimal d25;

    /**
     * 26日金额
     */
    private BigDecimal d26;

    /**
     * 27日金额
     */
    private BigDecimal d27;

    /**
     * 28日金额
     */
    private BigDecimal d28;

    /**
     * 29日金额
     */
    private BigDecimal d29;

    /**
     * 30日金额
     */
    private BigDecimal d30;

    /**
     * 45日金额
     */
    private BigDecimal d45;

    /**
     * 60日金额
     */
    private BigDecimal d60;

    /**
     * 90日金额
     */
    private BigDecimal d90;

    /**
     * 120日金额
     */
    private BigDecimal d120;

    /**
     * 150日金额
     */
    private BigDecimal d150;

    /**
     * 180日金额
     */
    private BigDecimal d180;

    /**
     * 270日金额
     */
    private BigDecimal d270;

    /**
     * 360日金额
     */
    private BigDecimal d360;

    /**
     * 创建时间
     */
    private Date createTime;


}
