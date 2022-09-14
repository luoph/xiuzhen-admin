package cn.youai.xiuzhen.stat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PayOrderGiftVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 自己方订单号
     */
    private String orderId;

    /**
     * 平台方订单号
     */
    private String queryId;

    /**
     * 渠道id
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 支付玩家id
     */
    private Long playerId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * ip地址
     */
    private String remoteIp;

    /**
     * 0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放
     */
    private Integer orderStatus;

    /**
     * 订单金额
     */
    private java.math.BigDecimal orderAmount;

    /**
     * 实际支付金额
     */
    private java.math.BigDecimal payAmount;

    /**
     * 折扣金额
     */
    private java.math.BigDecimal discountAmount;

    /**
     * 备注
     */
    private String custom;

    /**
     * 充值货币(CNY:人民币)
     */
    private String currency;

    /**
     * 订单创建时间戳
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date payTime;

    /**
     * 发货时间
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date sendTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;

    /**
     * 订单创建时间戳
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;

    /**
     * 道具数量（这件道具的总数）
     */
    private Integer productCount;

    /**
     * 该道具消费次数占总数的比例
     */
    private BigDecimal productCountRatio;

    /**
     * 该道具的消费金额总数
     */
    private BigDecimal payAmountSum;

    /**
     * 该件道具的消费金额占总消费金额的比例
     */
    private BigDecimal payAmountRatio;
}
