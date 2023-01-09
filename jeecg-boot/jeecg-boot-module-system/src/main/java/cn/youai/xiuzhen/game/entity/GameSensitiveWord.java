package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 敏感词
 * @date 2023-01-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_sensitive_word")
@ApiModel(value = "GameSensitiveWord", description = "敏感词")
public class GameSensitiveWord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 敏感词
     */
    @Excel(name = "敏感词", width = 15)
    private String word;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private String remark;
}
