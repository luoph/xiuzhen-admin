package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @description 充值订单
 * @date 2020-01-05
 */
@Data
@TableName("game_order")
@Accessors(chain = true)
public class GameOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 支付玩家id
     */
    @ExcelProperty("玩家id")
    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    /**
     * 己方订单号
     */
    @ColumnWidth(15)
    @ExcelProperty("支付订单号")
    @Excel(name = "支付订单号", width = 15)
    private String orderId;

    /**
     * 平台SDK订单号
     */
    @ColumnWidth(15)
    @ExcelProperty("平台订单号")
    @Excel(name = "平台订单号", width = 15)
    private String queryId;

    /**
     * 己方渠道id
     */
    @ColumnWidth(15)
    @ExcelProperty("渠道")
    @Excel(name = "渠道", width = 15)
    private String channel;

    /**
     * 服务器id
     */
    @ColumnWidth(15)
    @ExcelProperty("区服id")
    @Excel(name = "区服id", width = 15)
    private Integer serverId;

    /**
     * 商品id
     */
    @ColumnWidth(15)
    @ExcelProperty("商品id")
    @Excel(name = "商品id", width = 15)
    private String productId;

    /**
     * ip地址
     */
    @ColumnWidth(15)
    @ExcelProperty("ip地址")
    @Excel(name = "ip地址", width = 15)
    private String remoteIp;

    /**
     * 订单状态
     * 0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放
     */
    @ColumnWidth(15)
    @ExcelProperty("订单状态")
    @Excel(name = "订单状态", width = 15)
    private Integer orderStatus;

    /**
     * 实际支付金额
     */
    @ColumnWidth(15)
    @ExcelProperty("支付金额")
    @Excel(name = "支付金额", width = 15)
    private BigDecimal payAmount;

    /**
     * 订单金额，显示给用户看的
     */
    @ColumnWidth(15)
    @ExcelProperty("订单金额")
    @Excel(name = "订单金额", width = 15)
    private BigDecimal orderAmount;

    /**
     * 折扣金额
     */
    @ColumnWidth(15)
    @ExcelProperty("折扣金额")
    @Excel(name = "折扣金额", width = 15)
    private BigDecimal discountAmount;

    /**
     * 备注
     */
    @ColumnWidth(15)
    @ExcelProperty("透传参数")
    @Excel(name = "透传参数", width = 15)
    private String custom;

    /**
     * 充值货币(CNY:人民币)
     */
    @ColumnWidth(15)
    @ExcelProperty("充值货币")
    @Excel(name = "充值货币", width = 15)
    private String currency;

    /**
     * 订单创建时间戳
     */
    @ColumnWidth(15)
    @ExcelProperty("支付时间")
    @Excel(name = "支付时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date payTime;

    /**
     * 发货时间
     */
    @ColumnWidth(15)
    @ExcelProperty("发货时间")
    @Excel(name = "发货时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date sendTime;

    /**
     * 更新时间
     */
    @ColumnWidth(15)
    @ExcelProperty("更新时间")
    @Excel(name = "更新时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date updateTime;

    /**
     * 订单创建时间戳
     */
    @ColumnWidth(15)
    @ExcelProperty("创建时间")
    @Excel(name = "创建时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date createTime;

    @TableField(exist = false)
    @ExcelProperty("充值总额")
    @Excel(name = "充值总额", width = 15)
    private Double totalAmount;

    /**
     * 玩家名
     */
    @ExcelProperty("玩家名")
    @Excel(name = "玩家名", width = 15)
    @TableField(exist = false)
    private String nickname;

    @ExcelProperty("账号")
    @Excel(name = "账号", width = 15)
    @TableField(exist = false)
    private String account;

    @ExcelProperty("sdk渠道")
    @Excel(name = "sdk渠道", width = 15)
    @TableField(exist = false)
    private String sdkChannel;

    @TableField(exist = false)
    private Long vipId;
}
