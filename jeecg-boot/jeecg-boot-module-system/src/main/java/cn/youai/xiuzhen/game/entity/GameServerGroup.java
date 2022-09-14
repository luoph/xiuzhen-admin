/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏渠道服配置
 * </p>
 *
 * @author luopeihuan
 * @since 2021-08-03
 */
@Data
@Accessors(chain = true)
public class GameServerGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * GM地址
     */
    private String gmUrl;

    /**
     * 聊天服地址
     */
    private String chatServerUrl;

    /**
     * 跨服地址
     */
    private String crossServerUrl;

    /**
     * 跨服仙盟战开关
     */
    private Integer crossFactionWar;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;


}
