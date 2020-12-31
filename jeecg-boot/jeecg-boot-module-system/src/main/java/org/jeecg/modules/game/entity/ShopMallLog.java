package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 山城购买日志
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
@Data
@Accessors(chain = true)
public class ShopMallLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 所属商城类型
     */
    private Integer type;

    /**
     * 货架id
     */
    private Integer goodsId;

    /**
     * 道具id
     */
    private Integer itemId;

    /**
     * 购买状态 0成功 1失败
     */
    private Integer state;

    /**
     * 购买数量
     */
    private Long num;

    /**
     * 消耗总价
     */
    private Long totalPrice;

    /**
     * 消耗物品
     */
    private Integer priceItem;

    /**
     * 购买类型 0-商城 1-快捷购买
     */
    private Integer buyType;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间String格式
     */
    private String createTimeString;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    /**
     * 途径类型对应的名称
     */
    private String wayName;

    /**
     * 道具的数量汇总
     */
    private BigDecimal itemNum;

    /**
     * 玩家数量
     */
    private BigDecimal playerNum;

    /**
     * 对应途径下货币产销次数
     */
    private BigDecimal itemCount;

    /**
     * 对应途径下货币产销数量占比
     */
    private BigDecimal itemNumRate;

    /**
     * 全途径下的道具数量总和
     */
    private BigDecimal itemNumSum;


}
