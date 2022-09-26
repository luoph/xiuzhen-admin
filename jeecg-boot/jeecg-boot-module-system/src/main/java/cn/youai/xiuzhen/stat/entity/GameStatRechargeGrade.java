package cn.youai.xiuzhen.stat.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_order
 * @date 2020-09-29
 */
@Data
@Accessors(chain = true)
public class GameStatRechargeGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "渠道", width = 15)
    private String channel;

    /**
     * 服务器id
     */
    @Excel(name = "服务器id", width = 15)
    private Integer serverId;

    /**
     * 分档
     */
    @Excel(name = "分档", width = 15)
    private Integer grade;

    @Excel(name = "付费金额", width = 15)
    private BigDecimal payAmount;

    @Excel(name = "付费总金额", width = 15)
    private BigDecimal totalAmount;

    @Excel(name = "付费人数", width = 15)
    private Integer playerNum;

    @Excel(name = "付费总人数", width = 15)
    private Integer totalPlayerNum;

    @Excel(name = "付费次数", width = 15)
    private Integer payNum;

    @Excel(name = "付费总次数", width = 15)
    private Integer totalPayNum;

}
