package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏审核信息
 * @date 2023-02-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_review")
public class GameReview extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 游戏名称
     */
    @Excel(name = "名称", width = 15)
    private String name;

    /**
     * 游戏编号
     */
    @Excel(name = "游戏编号", width = 15)
    private Integer gameId;

    /**
     * sdk渠道
     */
    @Excel(name = "sdk渠道", width = 15)
    private String sdkChannel;

    /**
     * 版本
     */
    @Excel(name = "版本", width = 15)
    private String version;

    @Excel(name = "区服配置", width = 15)
    private String profile;

    @Excel(name = "状态", width = 15)
    private Integer status;

    /**
     * 描述
     */
    @Excel(name = "备注", width = 15)
    private String remark;
}
