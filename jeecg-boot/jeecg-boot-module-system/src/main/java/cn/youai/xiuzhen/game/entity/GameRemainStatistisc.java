/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 留存统计vo
 * </p>
 *
 * @author buliangliang
 * @since 2020-09-12
 */
@Data
public class GameRemainStatistisc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 新增的用户集合
     */
    private JSONArray userJsonArray;

    /**
     * 统计日期
     */
    private String countDate;

    /**
     * 统计类型 1-留存  2-ltv
     */
    private Integer type;

    /**
     * 新增玩家
     */
    private Long registerNum;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 2日留存
     */
    private Long c2;

    /**
     * 3留存
     */
    private Long c3;

    /**
     * 4留存
     */
    private Long c4;

    /**
     * 5留存
     */
    private Long c5;

    /**
     * 6留存
     */
    private Long c6;

    /**
     * 7留存
     */
    private Long c7;

    /**
     * 15留存
     */
    private Long c15;

    /**
     * 30留存
     */
    private Long c30;

    /**
     * 60留存
     */
    private Long c60;

    /**
     * 90留存
     */
    private Long c90;
    /**
     * 120留存
     */
    private Long c120;


}
