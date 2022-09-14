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
 * @description 服务器订单
 * @date 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PayOrderBillVO implements Serializable {

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
    private BigDecimal orderAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

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
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
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
     * 流水总额
     */
    private BigDecimal billSum;

    /**
     * 开始时间（用于页面展示冗余字段）
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private String beginTime;

    /**
     * 结束时间（用于页面展示冗余字段）
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private String endTime;

}
