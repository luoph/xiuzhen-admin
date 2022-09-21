/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 留存统计
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
@Data
@Accessors(chain = true)
public class GameStatRemainDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 渠道
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
     * 角色类型，0-全部，1-付费，2-免费
     */
    private Integer roleType;

    /**
     * 首日新增
     */
    private Integer d1;

    /**
     * 2日留存
     */
    private Integer d2;

    /**
     * 3日留存
     */
    private Integer d3;

    /**
     * 4日留存
     */
    private Integer d4;

    /**
     * 5日留存
     */
    private Integer d5;

    /**
     * 6日留存
     */
    private Integer d6;

    /**
     * 7日留存
     */
    private Integer d7;

    /**
     * 8日留存
     */
    private Integer d8;

    /**
     * 9日留存
     */
    private Integer d9;

    /**
     * 10日留存
     */
    private Integer d10;

    /**
     * 11日留存
     */
    private Integer d11;

    /**
     * 12日留存
     */
    private Integer d12;

    /**
     * 13日留存
     */
    private Integer d13;

    /**
     * 14日留存
     */
    private Integer d14;

    /**
     * 15日留存
     */
    private Integer d15;

    /**
     * 16日留存
     */
    private Integer d16;

    /**
     * 17日留存
     */
    private Integer d17;

    /**
     * 18日留存
     */
    private Integer d18;

    /**
     * 19日留存
     */
    private Integer d19;

    /**
     * 20日留存
     */
    private Integer d20;

    /**
     * 21日留存
     */
    private Integer d21;

    /**
     * 22日留存
     */
    private Integer d22;

    /**
     * 23日留存
     */
    private Integer d23;

    /**
     * 24日留存
     */
    private Integer d24;

    /**
     * 25日留存
     */
    private Integer d25;

    /**
     * 26日留存
     */
    private Integer d26;

    /**
     * 27日留存
     */
    private Integer d27;

    /**
     * 28日留存
     */
    private Integer d28;

    /**
     * 29日留存
     */
    private Integer d29;

    /**
     * 30日留存
     */
    private Integer d30;

    /**
     * 45日留存
     */
    private Integer d45;

    /**
     * 60日留存
     */
    private Integer d60;

    /**
     * 90日留存
     */
    private Integer d90;

    /**
     * 120日留存
     */
    private Integer d120;

    /**
     * 150日留存
     */
    private Integer d150;

    /**
     * 180日留存
     */
    private Integer d180;

    /**
     * 270日留存
     */
    private Integer d270;

    /**
     * 360日留存
     */
    private Integer d360;

    /**
     * 创建时间
     */
    private Date createTime;

    public GameStatRemainDetail apply(GameStatRemainDetail other) {
        setD1(other.getD1());
        return this;
    }

}
