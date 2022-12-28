package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 任务活动
 * @date 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GameCampaignTypeBase extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty("id")
    @Excel(name = "id", width = 15)
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * campaign.id, 活动id
     */
    @ExcelProperty("主活动id")
    @Excel(name = "主活动id", width = 15)
    private Long campaignId;

    /**
     * game_campaign_type.id
     */
    @ExcelProperty("子活动id")
    @Excel(name = "子活动id", width = 15)
    private Long typeId;
}
