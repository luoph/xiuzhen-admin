/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.utils.BigDecimalUtils;
import lombok.Data;
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
@Accessors(chain = true)
public class GameStat30Days implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Integer registerNum;

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
    private BigDecimal d1;

    /**
     * 2留存或者ltv
     */
    private BigDecimal d2;

    /**
     * 3留存或者ltv
     */
    private BigDecimal d3;

    /**
     * 4留存或者ltv
     */
    private BigDecimal d4;

    /**
     * 5留存或者ltv
     */
    private BigDecimal d5;

    /**
     * 6留存或者ltv
     */
    private BigDecimal d6;

    /**
     * 7留存或者ltv
     */
    private BigDecimal d7;

    /**
     * 8留存或者ltv
     */
    private BigDecimal d8;

    /**
     * 9留存或者ltv
     */
    private BigDecimal d9;

    /**
     * 10留存或者ltv
     */
    private BigDecimal d10;

    /**
     * 11留存或者ltv
     */
    private BigDecimal d11;

    /**
     * 12留存或者ltv
     */
    private BigDecimal d12;

    /**
     * 13留存或者ltv
     */
    private BigDecimal d13;

    /**
     * 14留存或者ltv
     */
    private BigDecimal d14;

    /**
     * 15留存或者ltv
     */
    private BigDecimal d15;

    /**
     * 16留存或者ltv
     */
    private BigDecimal d16;

    /**
     * 17留存或者ltv
     */
    private BigDecimal d17;

    /**
     * 18留存或者ltv
     */
    private BigDecimal d18;

    /**
     * 19留存或者ltv
     */
    private BigDecimal d19;

    /**
     * 20留存或者ltv
     */
    private BigDecimal d20;

    /**
     * 21留存或者ltv
     */
    private BigDecimal d21;

    /**
     * 22留存或者ltv
     */
    private BigDecimal d22;

    /**
     * 23留存或者ltv
     */
    private BigDecimal d23;

    /**
     * 24留存或者ltv
     */
    private BigDecimal d24;

    /**
     * 25留存或者ltv
     */
    private BigDecimal d25;

    /**
     * 26留存或者ltv
     */
    private BigDecimal d26;

    /**
     * 27留存或者ltv
     */
    private BigDecimal d27;

    /**
     * 28留存或者ltv
     */
    private BigDecimal d28;

    /**
     * 29留存或者ltv
     */
    private BigDecimal d29;

    /**
     * 30留存或者ltv
     */
    private BigDecimal d30;

    public static GameStat30Days from(GameStatRemainDetail detail) {
        return new GameStat30Days().setType(1)
                .setCountDate(detail.getCountDate())
                .setRegisterNum(detail.getD1())
                .setChannel(detail.getChannel())
                .setServerId(detail.getServerId())
                .setD1(BigDecimalUtils.from(detail.getD1()))
                .setD2(BigDecimalUtils.from(detail.getD2()))
                .setD3(BigDecimalUtils.from(detail.getD3()))
                .setD4(BigDecimalUtils.from(detail.getD4()))
                .setD5(BigDecimalUtils.from(detail.getD5()))
                .setD6(BigDecimalUtils.from(detail.getD6()))
                .setD7(BigDecimalUtils.from(detail.getD7()))
                .setD8(BigDecimalUtils.from(detail.getD8()))
                .setD9(BigDecimalUtils.from(detail.getD9()))
                .setD10(BigDecimalUtils.from(detail.getD10()))
                .setD11(BigDecimalUtils.from(detail.getD11()))
                .setD12(BigDecimalUtils.from(detail.getD12()))
                .setD13(BigDecimalUtils.from(detail.getD13()))
                .setD14(BigDecimalUtils.from(detail.getD14()))
                .setD15(BigDecimalUtils.from(detail.getD15()))
                .setD16(BigDecimalUtils.from(detail.getD16()))
                .setD17(BigDecimalUtils.from(detail.getD17()))
                .setD18(BigDecimalUtils.from(detail.getD18()))
                .setD19(BigDecimalUtils.from(detail.getD19()))
                .setD20(BigDecimalUtils.from(detail.getD20()))
                .setD21(BigDecimalUtils.from(detail.getD21()))
                .setD22(BigDecimalUtils.from(detail.getD22()))
                .setD23(BigDecimalUtils.from(detail.getD23()))
                .setD24(BigDecimalUtils.from(detail.getD24()))
                .setD25(BigDecimalUtils.from(detail.getD25()))
                .setD26(BigDecimalUtils.from(detail.getD26()))
                .setD27(BigDecimalUtils.from(detail.getD27()))
                .setD28(BigDecimalUtils.from(detail.getD28()))
                .setD29(BigDecimalUtils.from(detail.getD29()))
                .setD30(BigDecimalUtils.from(detail.getD30()));

    }


    public static GameStat30Days from(GameStatLtvDetail detail) {
        return new GameStat30Days().setType(2)
                .setCountDate(detail.getCountDate())
                .setRegisterNum(detail.getNum())
                .setChannel(detail.getChannel())
                .setServerId(detail.getServerId())
                .setD1(detail.getD1())
                .setD2(detail.getD2())
                .setD3(detail.getD3())
                .setD4(detail.getD4())
                .setD5(detail.getD5())
                .setD6(detail.getD6())
                .setD7(detail.getD7())
                .setD8(detail.getD8())
                .setD9(detail.getD9())
                .setD10(detail.getD10())
                .setD11(detail.getD11())
                .setD12(detail.getD12())
                .setD13(detail.getD13())
                .setD14(detail.getD14())
                .setD15(detail.getD15())
                .setD16(detail.getD16())
                .setD17(detail.getD17())
                .setD18(detail.getD18())
                .setD19(detail.getD19())
                .setD20(detail.getD20())
                .setD21(detail.getD21())
                .setD22(detail.getD22())
                .setD23(detail.getD23())
                .setD24(detail.getD24())
                .setD25(detail.getD25())
                .setD26(detail.getD26())
                .setD27(detail.getD27())
                .setD28(detail.getD28())
                .setD29(detail.getD29())
                .setD30(detail.getD30());
    }
}
