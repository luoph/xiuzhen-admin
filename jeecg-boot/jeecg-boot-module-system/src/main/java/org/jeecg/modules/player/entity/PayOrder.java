package org.jeecg.modules.player.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Data
@TableName("pay_order")
@Accessors(chain = true)
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 自己方订单号
     */
    @Excel(name = "自己方订单号", width = 15)
    private java.lang.String queryId;

    /**
     * 平台方订单号
     */
    @Excel(name = "平台方订单号", width = 15)
    private java.lang.String orderId;

    /**
     * 渠道key
     */
    @Excel(name = "渠道key", width = 15)
    private java.lang.String channelKey;

    /**
     * 渠道id
     */
    @Excel(name = "渠道id", width = 15)
    private java.lang.Integer channelId;

    /**
     * 区服Id
     */
    @Excel(name = "区服Id", width = 15)
    private java.lang.Integer serverId;

    /**
     * 支付玩家id
     */
    @Excel(name = "支付玩家id", width = 15)
    private java.lang.Long playerId;

    /**
     * 商品id
     */
    @Excel(name = "商品id", width = 15)
    private java.lang.String goodsId;

    /**
     * ip地址
     */
    @Excel(name = "ip地址", width = 15)
    private java.lang.String remoteIp;

    /**
     * 订单状态
     * 0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放
     */
    @Excel(name = "订单状态", width = 15)
    @Dict(dicCode = "order_status")
    private java.lang.Integer orderStatus;

    /**
     * 充值金额(如有商品 ID,此值为商 品的金额, 单位元)
     */
    @Excel(name = "充值金额", width = 15)
    private java.math.BigDecimal payAmount;

    /**
     * 实际金额
     */
    @Excel(name = "实际金额", width = 15)
    private java.math.BigDecimal realAmount;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String custom;

    /**
     * extra
     */
    @Excel(name = "extra", width = 15)
    private java.math.BigDecimal extra;

    /**
     * 充值货币(CNY:人民币)
     */
    @Excel(name = "充值货币", width = 15)
    private java.lang.String currency;

    /**
     * 回调给游戏服时间
     */
    @Excel(name = "回调时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date postTime;

    /**
     * 发货时间
     */
    @Excel(name = "发货时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date sendTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date updateTime;

    /**
     * 订单创建时间戳
     */
    @Excel(name = "订单创建时间戳", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;
}
