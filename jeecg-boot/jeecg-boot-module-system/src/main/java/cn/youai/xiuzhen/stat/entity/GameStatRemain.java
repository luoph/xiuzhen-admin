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
     * 角色类型
     */
    private Integer roleType;

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
     * 2日留存率
     */
    private Integer d2;

    /**
     * 3日留存率
     */
    private Integer d3;

    /**
     * 4日留存率
     */
    private Integer d4;

    /**
     * 5日留存率
     */
    private Integer d5;

    /**
     * 6日留存率
     */
    private Integer d6;

    /**
     * 7日留存率
     */
    private Integer d7;

    /**
     * 15日留存率
     */
    private Integer d15;

    /**
     * 30日留存率
     */
    private Integer d30;

    /**
     * 60日留存率
     */
    private Integer d60;

    /**
     * 90日留存率
     */
    private Integer d90;

    /**
     * 120日留存率
     */
    private Integer d120;

    /**
     * 180日留存率
     */
    private Integer d180;

    /**
     * 360日留存率
     */
    private Integer d360;

    /**
     * 创建时间
     */
    private Date createTime;

}
