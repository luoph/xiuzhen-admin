package org.jeecg.modules.game.entity;

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
 * @description 游戏图片
 * @date 2020-11-02
 */
@Data
@TableName("game_image")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameImage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 图片类型: 1.icon, 2.banner
     */
    @Excel(name = "图片类型", width = 15)
    private java.lang.Integer type;

    /**
     * 图片名
     */
    @Excel(name = "图片名", width = 15)
    private java.lang.String name;

    /**
     * 相对路径
     */
    @Excel(name = "相对路径", width = 15)
    private java.lang.String imgUrl;

    /**
     * 图片宽px
     */
    @Excel(name = "图片宽px", width = 15)
    private java.lang.Integer width;

    /**
     * 图片高px
     */
    @Excel(name = "图片高px", width = 15)
    private java.lang.Integer height;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;
}
