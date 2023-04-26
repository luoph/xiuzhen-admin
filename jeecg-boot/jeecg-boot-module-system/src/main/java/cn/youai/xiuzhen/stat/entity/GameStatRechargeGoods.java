package cn.youai.xiuzhen.stat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameStatRechargeGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "统计日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date countDate;

    /**
     * 渠道id
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品分组
     */
    private Integer rechargeGroup;

    /**
     * 商品id
     */
    private String productName;

    /**
     * 消费次数
     */
    private Integer payNum;

    /**
     * 消费玩家数
     */
    private Integer playerNum;

    /**
     * 消费金额
     */
    private BigDecimal payAmount;

    /**
     * 消费次数
     */
    private Integer totalNum;

    /**
     * 总消费玩家数
     */
    private Integer totalPlayerNum;

    /**
     * 消费金额
     */
    private BigDecimal totalAmount;

}
