/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;

/**
 * <p>
 * 游戏渠道服配置
 * </p>
 *
 * @author luopeihuan
 * @since 2021-04-22
 */
@Data
@Accessors(chain = true)
@TableName("game_server_group_item")
@EqualsAndHashCode(callSuper = true)
public class GameServerGroupItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 区服id
     */
    private Integer serverId;
}
