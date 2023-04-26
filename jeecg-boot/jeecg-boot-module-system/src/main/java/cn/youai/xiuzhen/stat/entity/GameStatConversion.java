/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.entity;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.utils.BigDecimalUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 新增转化统计
 * </p>
 *
 * @author luopeihuan
 * @since 2023-01-17
 */
@Data
@Accessors(chain = true)
public class GameStatConversion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 渠道标记
     */
    private String channel;

    /**
     * Sdk渠道
     */
    private String sdkChannel;

    /**
     * 新增账号数
     */
    private Integer newAccountNum;

    /**
     * 新增角色数
     */
    private Integer newPlayerNum;

    /**
     * 新增注册玩家支付数
     */
    private Integer newPlayerPayNum;

    /**
     * 新增注册玩家支付率
     */
    private BigDecimal newPlayerPayRate;

    /**
     * 账号角色转化率
     */
    private BigDecimal newConversionRate;

    /**
     * 统计日期
     */
    @Excel(name = "统计日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date countDate;

    /**
     * 创建时间
     */
    private Date createTime;

    public static GameStatConversion of(String channel,
                                        String sdkChannel,
                                        Integer serverId,
                                        Date date,
                                        int newAccountNum,
                                        int newPlayerNum,
                                        int newPlayerPayNum) {
        return new GameStatConversion()
                .setChannel(StatisticType.channel(channel))
                .setServerId(StatisticType.serverId(serverId))
                .setSdkChannel(StatisticType.channel(sdkChannel))
                .setCountDate(date)
                .setNewAccountNum(newAccountNum)
                .setNewPlayerNum(newPlayerNum)
                .setNewPlayerPayNum(newPlayerPayNum)
                .setCreateTime(DateUtils.now())
                .calc();
    }

    public GameStatConversion calc() {
        setNewPlayerPayRate(BigDecimalUtils.divideZero(newPlayerPayNum, newPlayerNum, true))
                .setNewConversionRate(BigDecimalUtils.divideZero(newPlayerNum, newAccountNum, true));
        return this;
    }

}
