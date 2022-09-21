package cn.youai.xiuzhen.stat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

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
     * 统计日期
     */
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

}
