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
 * @description 游戏设置
 * @date 2020-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_setting")
@Accessors(chain = true)
public class GameSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * key
     */
    @Excel(name = "key", width = 15)
    private java.lang.String dictKey;

    /**
     * value
     */
    @Excel(name = "value", width = 15)
    private java.lang.String dictValue;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private java.lang.String remark;
}
