package org.jeecg.modules.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动-开服排行-类型库
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign_rank_type")
@EqualsAndHashCode(callSuper = true)
public class OpenServiceCampaignRankType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaignRankType() {
    }

    public OpenServiceCampaignRankType(OpenServiceCampaignRankType other) {
//        this.id = other.id;
        this.rankType = other.rankType;
        this.rankTypeName = other.rankTypeName;
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 排行类型 e.g. 1.境界冲榜, 2.功法冲榜
     */
    @Excel(name = "排行类型", width = 15)
    private java.lang.Integer rankType;

    /**
     * 排行类型名称
     */
    @Excel(name = "排行类型名称", width = 15)
    private java.lang.String rankTypeName;
}
