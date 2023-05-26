package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.utils.BigDecimalUtils;
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
 * @author jeecg-boot
 * @version V1.0
 * @description game_order
 * @date 2020-09-29
 */
@Data
@Accessors(chain = true)
public class ServerBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道名
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 玩家数
     */
    private Integer playerNum;

    /**
     * 人均充值
     */
    private BigDecimal playerAvg;

    /**
     * 区服数
     */
    private Integer serverNum;

    /**
     * 服均充值
     */
    private BigDecimal serverAvg;

    /**
     * 统计日期
     */
    @Excel(name = "统计日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date countDate;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 累计支付金额
     */
    private BigDecimal totalAmount;

    public ServerBill calc() {
        this.playerAvg = BigDecimalUtils.divideZero(totalAmount, BigDecimal.valueOf(playerNum), false);
        this.serverAvg = BigDecimalUtils.divideZero(totalAmount, BigDecimal.valueOf(serverNum), false);
        return this;
    }

}
