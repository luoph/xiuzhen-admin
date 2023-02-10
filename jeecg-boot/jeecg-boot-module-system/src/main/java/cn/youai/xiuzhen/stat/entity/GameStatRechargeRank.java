package cn.youai.xiuzhen.stat.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Data
@Accessors(chain = true)
public class GameStatRechargeRank implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    @Excel(name = "区服id", width = 15)
    private Integer serverId;

    @Excel(name = "渠道", width = 15)
    private String channel;

    @Excel(name = "Sdk渠道", width = 15)
    private String sdkChannel;

    @Excel(name = "玩家昵称", width = 15)
    private String nickname;

    @Excel(name = "排名", width = 15)
    private Integer rank;

    @Excel(name = "玩家等级", width = 15)
    private Integer level;

    /**
     * 充值金额
     */
    @Excel(name = "充值总金额", width = 15)
    private BigDecimal payAmount;

    @Excel(name = "最近充值", width = 15)
    private BigDecimal lastPay;

    @Excel(name = "创角时间", width = 15)
    private Date createTime;

    @Excel(name = "最后登录时间", width = 15)
    private Date lastLoginTime;

    @Excel(name = "最后充值时间", width = 15)
    private Date lastPayTime;

    @Excel(name = "创角天数", width = 15)
    private Integer playDays;

    @Excel(name = "登录预警天数", width = 15)
    private Integer lastLoginDays;

    @Excel(name = "充值预警天数", width = 15)
    private Integer lastPayDays;

    private Long orderId;

    private Date countDate;

    private Long vipId;
}
