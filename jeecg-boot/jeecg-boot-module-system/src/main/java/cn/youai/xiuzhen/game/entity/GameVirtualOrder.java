package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟充值订单
 * @date 2020-09-02
 */
@Data
@Accessors(chain = true)
@TableName("game_virtual_order")
@EqualsAndHashCode(callSuper = true)
public class GameVirtualOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 区服id
     */
    @Excel(name = "区服id", width = 15)
    private java.lang.Integer serverId;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private java.lang.Long playerId;

    /**
     * 玩家名
     */
    @ExcelProperty("玩家名")
    @Excel(name = "玩家名", width = 15)
    @TableField(exist = false)
    private String playerName;

    /**
     * 商品id
     */
    @Excel(name = "商品id", width = 15)
    private java.lang.Integer goodsId;

    @TableField(exist = false)
    private String goodsIds;

    @TableField(exist = false)
    private String goodsName;

    /**
     * 状态，0-失败 1-成功
     */
    @Excel(name = "状态", width = 15)
    private java.lang.Integer status;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;

}
