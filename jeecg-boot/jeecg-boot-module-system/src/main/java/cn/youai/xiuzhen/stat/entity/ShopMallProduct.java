package cn.youai.xiuzhen.stat.entity;

import cn.hutool.core.util.StrUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.base.IServerData;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;

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
public class ShopMallProduct implements IServerData, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 统计标识
     */
    @Excel(name = "时间", width = 15)
    private String statTime;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 商店类型id
     */
    private Integer typeId;

    /**
     * 页签id
     */
    @Excel(name = "页签id", width = 15)
    private Integer tabId;

    /**
     * 道具id
     */
    @Excel(name = "道具id", width = 15)
    private Integer itemId;

    /**
     * 道具数量
     */
    @Excel(name = "道具数量", width = 15)
    private Integer itemNum;

    /**
     * 道具名
     */
    @Excel(name = "道具名", width = 15)
    private String itemName;

    /**
     * 货币id
     */
    @Excel(name = "货币id", width = 15)
    private Integer costItem;

    /**
     * 货币名称
     */
    @Excel(name = "货币名称", width = 15)
    private String costName;

    /**
     * 货币数量（对应商店购买此道具花费的货币数量）
     */
    @Excel(name = "货币数量", width = 15)
    private Long costNum;

    /**
     * 购买玩家数（购买过此道具的玩家数量）
     */
    @Excel(name = "购买玩家数", width = 15)
    private Integer playerNum;

    /**
     * 购买次数
     */
    @Excel(name = "购买次数", width = 15)
    private Long buyNum;

    /**
     * 全道具货币数量
     */
    @Excel(name = "全道具货币数量", width = 15)
    private Long totalNum;

    /**
     * 购买总次数
     */
    @Excel(name = "购买总次数", width = 15)
    private Long totalBuyNum;

    public String groupByTypeAndDate() {
        return StrUtil.join(":", getTypeId(), DateUtils.formatDate(getCountDate(), TimeConstant.DEFAULT_DATE_FORMAT));
    }

    public String groupByTypeAndItemId() {
        return StrUtil.join(":", getTypeId(), getItemId());
    }

}
