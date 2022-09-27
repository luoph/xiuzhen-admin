package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.core.base.IServerData;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商城购买日志
 * </p>
 *
 * @author buliangliang
 * @since 2020-04-13
 */
@Data
@Accessors(chain = true)
public class ShopMallRecord implements IServerData, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 商城类型
     */
    private Integer type;

    /**
     * 货架id
     */
    private Integer shelfId;

    /**
     * 道具id
     */
    private Integer itemId;

    /**
     * 购买数量
     */
    private Long itemNum;

    /**
     * 消耗物品
     */
    private Integer costItem;

    /**
     * 消耗物品数量
     */
    private Long costNum;

    /**
     * 购买类型 0-商城 1-快捷购买
     */
    private Integer buyType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date createDate;
}
