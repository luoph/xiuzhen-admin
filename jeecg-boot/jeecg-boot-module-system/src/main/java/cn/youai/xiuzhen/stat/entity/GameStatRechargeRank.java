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

    private String channel;

    private Integer serverId;

    private Date countDate;

    /**
     * 支付玩家id
     */
    @Excel(name = "玩家ID", width = 15)
    private Long playerId;

    @Excel(name = "玩家昵称", width = 15)
    private String nickname;

    @Excel(name = "玩家等级", width = 15)
    private Integer level;

    @Excel(name = "排名", width = 15)
    private Integer rank;

    /**
     * 充值金额
     */
    @Excel(name = "充值金额", width = 15)
    private BigDecimal payAmount;

}
