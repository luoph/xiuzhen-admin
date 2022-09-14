package cn.youai.xiuzhen.stat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 玩家表
 * </p>
 *
 * @author ocean
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 注册时间开始
     */
    private String createBegin;

    /**
     * 注册时间开始日期
     */
    private Date createDateBegin;

    /**
     * 注册时间结束
     */
    private String createEnd;

    /**
     * 注册时间结束日期
     */
    private Date createDateEnd;

    /**
     * 登录时间开始
     */
    private String loginBegin;

    /**
     * 登录时间开始日期
     */
    private Date loginDateBegin;

    /**
     * 登录时间结束
     */
    private String loginEnd;

    /**
     * 登录时间结束日期
     */
    private Date loginDateEnd;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 等级范围
     */
    private String level;

    /**
     * 等级范围开始
     */
    private Integer levelBegin;

    /**
     * 等级范围结束
     */
    private Integer levelEnd;

    /**
     * 充值范围
     */
    private String recharge;

    /**
     * 充值范围开始
     */
    private Double rechargeBegin;

    /**
     * 充值范围结束
     */
    private Double rechargeEnd;


}
