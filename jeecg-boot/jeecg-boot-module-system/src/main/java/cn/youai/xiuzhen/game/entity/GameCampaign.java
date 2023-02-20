package cn.youai.xiuzhen.game.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.utils.StringUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 活动配置
 * @date 2020-10-15
 */
@Data
@TableName("game_campaign")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GameCampaign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public GameCampaign() {
    }

    public GameCampaign(GameCampaign other) {
        this.type = other.getType();
        this.name = other.getName();
        this.description = other.getDescription();
        this.showName = other.getShowName();
        this.icon = other.getIcon();
        this.banner = other.getBanner();
        this.status = other.getStatus();
        this.priority = other.getPriority();
        this.autoOpen = other.getAutoOpen();
        this.autoAddServer = other.getAutoAddServer();
        this.timeType = other.getTimeType();
        this.startTime = other.getStartTime();
        this.endTime = other.getEndTime();
        this.startDay = other.getStartDay();
        this.duration = other.getDuration();
    }

    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动类型: 1.节日活动
     */
    @Excel(name = "活动类型", width = 15)
    private Integer type;

    /**
     * 活动名称，后台显示
     */
    @Excel(name = "活动名称（后台显示）", width = 15)
    private String name;

    /**
     * 活动标语（描述）
     */
    @Excel(name = "活动标语（描述）", width = 15)
    private String description;

    /**
     * 活动名称，游戏中显示
     */
    @Excel(name = "活动名称（游戏中显示）", width = 15)
    private String showName;

    /**
     * 活动图标
     */
    @Excel(name = "活动图标", width = 15)
    private String icon;

    /**
     * 活动宣传图
     */
    @Excel(name = "活动宣传图", width = 15)
    private String banner;

    /**
     * 活动状态: 0.关闭, 1.开启（备用字段，默认全部为开启）
     */
    @Excel(name = "活动状态", width = 15)
    private Integer status;

    /**
     * 优先级
     */
    @Excel(name = "优先级", width = 15)
    private Integer priority;

    /**
     * 自动开启
     */
    @Excel(name = "自动开启", width = 15)
    private Integer autoOpen;

    /**
     * 区服Id 使用,分割
     */
    @Excel(name = "区服Id", width = 15)
    private String serverIds;

    /**
     * sdk渠道 使用,分割
     */
    @Excel(name = "sdk渠道", width = 15)
    private String sdkChannels;

    /**
     * 是否自动添加新服
     */
    @Excel(name = "是否自动添加新服", width = 15)
    private Integer autoAddServer;

    /**
     * 时间类型: 1.具体时间范围, 2.开服第N天
     */
    @Excel(name = "时间类型", width = 15)
    private Integer timeType;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date endTime;

    /**
     * 开始时间(开服第n天, e.g. 0表示开服第1天)
     */
    @Excel(name = "开始天数", width = 15)
    private Integer startDay;

    /**
     * 持续时间(天)
     */
    @Excel(name = "持续天数", width = 15)
    private Integer duration;

    @TableField(exist = false)
    private List<GameCampaignType> typeList;

    @SuppressWarnings("DuplicatedCode")
    public boolean addServerId(Collection<Integer> serverIds) {
        List<Integer> all = StringUtils.split2Int(getServerIds());
        Collection<Integer> subtract = CollUtil.subtract(all, serverIds);
        if (CollUtil.isEmpty(subtract)) {
            return false;
        }
        all.addAll(serverIds);
        Collections.sort(all);
        setServerIds(StrUtil.join(",", all));
        return true;
    }
}
