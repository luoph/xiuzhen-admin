package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description pay_order
 * @date 2020-09-29
 */
@Data
@TableName("pay_order")
@Accessors(chain = true)
public class PayOrderBill implements Serializable {

    private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
    private java.lang.Integer id;
	
	/**
	 * 自己方订单号
	 */
	@Excel(name = "自己方订单号", width = 15)
    private java.lang.String orderId;
	
	/**
	 * 平台方订单号
	 */
	@Excel(name = "平台方订单号", width = 15)
    private java.lang.String queryId;
	
	/**
	 * 渠道id
	 */
	@Excel(name = "渠道id", width = 15)
    private java.lang.String channel;
	
	/**
	 * 渠道key
	 */
	@Excel(name = "渠道key", width = 15)
    private java.lang.String channelKey;
	
	/**
	 * 服务器id
	 */
	@Excel(name = "服务器id", width = 15)
    private java.lang.Integer serverId;
	
	/**
	 * 支付玩家id
	 */
	@Excel(name = "支付玩家id", width = 15)
    private java.lang.Integer playerId;
	
	/**
	 * 商品id
	 */
	@Excel(name = "商品id", width = 15)
    private java.lang.String productId;
	
	/**
	 * ip地址
	 */
	@Excel(name = "ip地址", width = 15)
    private java.lang.String remoteIp;
	
	/**
	 * 0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放
	 */
	@Excel(name = "0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放", width = 15)
    private java.lang.Integer orderStatus;
	
	/**
	 * 订单金额
	 */
	@Excel(name = "订单金额", width = 15)
    private java.math.BigDecimal orderAmount;
	
	/**
	 * 实际支付金额
	 */
	@Excel(name = "实际支付金额", width = 15)
    private java.math.BigDecimal payAmount;
	
	/**
	 * 折扣金额
	 */
	@Excel(name = "折扣金额", width = 15)
    private java.math.BigDecimal discountAmount;
	
	/**
	 * 备注
	 */
	@Excel(name = "备注", width = 15)
    private java.lang.String custom;
	
	/**
	 * 充值货币(CNY:人民币)
	 */
	@Excel(name = "充值货币(CNY:人民币)", width = 15)
    private java.lang.String currency;
	
	/**
	 * 订单创建时间戳
	 */
	@Excel(name = "订单创建时间戳", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date payTime;
	
	/**
	 * 发货时间
	 */
	@Excel(name = "发货时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date sendTime;
	
	/**
	 * 更新时间
	 */
	@Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date updateTime;
	
	/**
	 * 订单创建时间戳
	 */
	@Excel(name = "订单创建时间戳", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;
}
