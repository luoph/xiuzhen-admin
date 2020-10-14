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
 * @description 今日礼包
 * @date 2020-10-13
 */
@Data
@TableName("recharge_order")
@Accessors(chain = true)
public class RechargeOrder implements Serializable {

    private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
    private Integer id;
	
	/**
	 * 支付玩家id
	 */
	@Excel(name = "支付玩家id", width = 15)
    private Long playerId;
	
	/**
	 * 己方订单号
	 */
	@Excel(name = "己方订单号", width = 15)
    private String orderId;
	
	/**
	 * 平台方订单号
	 */
	@Excel(name = "平台方订单号", width = 15)
    private String queryId;
	
	/**
	 * 商品id
	 */
	@Excel(name = "商品id", width = 15)
    private Integer goodsId;

	/**
	 * 商品类型
	 */
	@Excel(name = "商品类型", width = 15)
	private Integer goodsType;
	
	/**
	 * 下发的商品
	 */
	@Excel(name = "下发的商品", width = 15)
    private String items;
	
	/**
	 * 首次额外赠送
	 */
	@Excel(name = "首次额外赠送", width = 15)
    private String addition;
	
	/**
	 * ip地址
	 */
	@Excel(name = "ip地址", width = 15)
    private String remoteIp;
	
	/**
	 * 0 - 未处理，1 - 已处理
	 */
	@Excel(name = "0 - 未处理，1 - 已处理", width = 15)
    private Integer status;
	
	/**
	 * 1 - 正常充值 2 - 虚拟充值
	 */
	@Excel(name = "1 - 正常充值 2 - 虚拟充值", width = 15)
    private Integer type;
	
	/**
	 * 实际支付金额
	 */
	@Excel(name = "实际支付金额", width = 15)
    private java.math.BigDecimal payAmount;
	
	/**
	 * 扩展自定义字段
	 */
	@Excel(name = "扩展自定义字段", width = 15)
    private String custom;
	
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
	 * 创建时间戳
	 */
	@Excel(name = "创建时间戳", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
	@JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private java.util.Date createTime;
}
