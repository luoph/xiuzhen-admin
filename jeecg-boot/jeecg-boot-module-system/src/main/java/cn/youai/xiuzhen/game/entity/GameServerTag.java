package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服标签
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_server_tag")
@ApiModel(value = "GameServerTag", description = "游戏服标签")
public class GameServerTag extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @Excel(name = "id", width = 15)
    @TableId(type = IdType.AUTO)
    private java.lang.Integer id;

    /**
     * 标签名
     */
    @Excel(name = "标签名", width = 15)
    private java.lang.String name;

    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    private java.lang.Integer position;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;
}
