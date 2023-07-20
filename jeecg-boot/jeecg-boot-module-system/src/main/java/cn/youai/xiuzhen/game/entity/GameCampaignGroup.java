package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 节日活动分组
 * @Author: jeecg-boot
 * @Date: 2023-07-20
 * @Version: V1.0
 */
@Data
@TableName("game_campaign_group")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "game_campaign_group对象", description = "节日活动分组")
public class GameCampaignGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 分组名称
     */
    @Excel(name = "分组名称", width = 15)
    @ApiModelProperty(value = "分组名称")
    private String name;
}
