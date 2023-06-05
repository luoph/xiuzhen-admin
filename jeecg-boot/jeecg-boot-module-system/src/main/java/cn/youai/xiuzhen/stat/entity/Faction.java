/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.core.base.IServerData;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 仙宗
 * </p>
 *
 * @author ocean
 * @since 2020-04-11
 */
@Data
public class Faction implements Serializable, IServerData {

    private static final long serialVersionUID = 1L;

    public Faction() {

    }

    public Faction(Long id) {
        this.id = id;
    }

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Excel(name = "区服id", width = 15)
    @TableField(exist = false)
    private Integer serverId;

    /**
     * 仙宗id
     */
    @Excel(name = "仙宗id", width = 15)
    private Long factionId;

    /**
     * 仙宗名称
     */
    @Excel(name = "仙宗名称", width = 15)
    @TableField(value = "`name`")
    private String name;

    /**
     * 盟主id
     */
    @Excel(name = "宗主id", width = 15)
    private Long playerId;

    @Excel(name = "宗主名称", width = 15)
    @TableField(exist = false)
    private String playerName;

    /**
     * 仙宗图腾id
     */
    @Excel(name = "仙宗图腾", width = 15)
    private Integer totemId;

    /**
     * 仙宗公告
     */
    @Excel(name = "公告", width = 15)
    private String notice;

    /**
     * 仙宗等级
     */
    @Excel(name = "仙宗等级", width = 15)
    private Integer factionLevel;

    /**
     * 是否满员
     */
    @Excel(name = "是否满员", width = 15)
    private Boolean fullMemberNum;

    /**
     * 仙宗人数
     */
    @Excel(name = "仙宗人数", width = 15)
    private Integer memberNum;

    /**
     * 副盟主人数
     */
    @Excel(name = "副盟主人数", width = 15)
    private Integer viceLeaderNum;

    /**
     * 长老人数
     */
    @Excel(name = "长老人数", width = 15)
    private Integer elderNum;

    /**
     * 经验
     */
    @Excel(name = "经验", width = 15)
    private Long exp;

    /**
     * 神体膜拜经验值
     */
    @Excel(name = "神体膜拜经验值", width = 15)
    private Long godBodyKneelExp;

    /**
     * 总仙力值
     */
    @Excel(name = "总仙力值", width = 15)
    private Long combatPower;

    /**
     * 免审核加入境界设置(faction_parameter.pass_level), 0表示关闭
     */
    @Excel(name = "免审核加入境界设置", width = 15)
    private Integer unauditedJoinLevel;

    /**
     * 捐献进度
     */
    @Excel(name = "捐献进度", width = 15)
    private Long contributionValue;

    /**
     * 捐献级别
     */
    @Excel(name = "捐献级别", width = 15)
    private Integer contributionLevel;

    /**
     * 恢复时间
     */
    @Excel(name = "恢复时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date recoverTime;

    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date createTime;

    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date updateTime;
}
