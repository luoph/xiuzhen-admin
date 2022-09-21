/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 留存统计
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GameStatRemain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 渠道名
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 统计日期
     */
    private Date countDate;

    /**
     * 新增角色数
     */
    private Integer registerNum;

    /**
     * 付费用户数
     */
    private Integer payNum;

    /**
     * 首日免费角色
     */
    private Integer freeNum;

    /**
     * 付费角色次留
     */
    private Integer payRemain;

    /**
     * 免费角色次留
     */
    private Integer freeRemain;

    /**
     * 新增角色数
     */
    private Integer registerRemain;

    /**
     * 创建时间
     */
    private Date createTime;

    public GameStatRemain apply(GameStatRemain other) {
        // 付费玩家数量
        setRegisterNum(other.getRegisterNum());
        setPayNum(other.getPayNum());
        setFreeNum(other.getFreeNum());
        setPayRemain(other.getPayRemain());
        setFreeRemain(other.getFreeRemain());
        setRegisterRemain(other.getRegisterRemain());
        return this;
    }

}
