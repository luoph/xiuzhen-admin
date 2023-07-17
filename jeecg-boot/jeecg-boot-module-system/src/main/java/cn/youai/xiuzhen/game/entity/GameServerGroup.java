/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.base.entity.BaseEntity;

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
@TableName("game_server_group")
@EqualsAndHashCode(callSuper = true)
public class GameServerGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * host
     */
    private String host;

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
     * 跨服结算时间
     */
    private Date crossSettleTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 区服id
     */
    @TableField(exist = false)
    private String serverIds;

    /**
     * 区服数
     */
    @TableField(exist = false)
    private Integer serverNum;

    /**
     * 在线数
     */
    @TableField(exist = false)
    private Integer onlineNum;

    /**
     * hostname
     */
    @TableField(exist = false)
    private String hostname;

    @JSONField(serialize = false, deserialize = false)
    public boolean skipCheck() {
        return StringUtils.contains(getGmUrl(), "localhost") || StringUtils.contains(getGmUrl(), "127.0.0.1");
    }

}
