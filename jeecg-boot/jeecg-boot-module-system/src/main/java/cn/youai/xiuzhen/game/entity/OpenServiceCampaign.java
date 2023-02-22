package cn.youai.xiuzhen.game.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.utils.StringUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 开服活动(1级)
 * @date 2020-12-21
 */
@Data
@TableName("game_open_service_campaign")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenServiceCampaign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OpenServiceCampaign() {
    }

    public OpenServiceCampaign(OpenServiceCampaign other) {
//        this.id = other.id;
        this.name = other.name;
        this.cross = other.cross;
//        this.serverIds = other.serverIds;
//        this.lastServerIds = other.lastServerIds;
        this.autoAddServer = other.autoAddServer;
        this.icon = other.icon;
        this.status = other.status;
        this.priority = other.priority;
        this.autoOpen = other.autoOpen;
        this.remark = other.remark;
    }

    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 15)
    private java.lang.String name;

    /**
     * 是否跨服
     */
    @Excel(name = "是否跨服", width = 15)
    @TableField(value = "`cross`")
    private Integer cross;

    /**
     * 服务器id，使用,分割
     */
    @Excel(name = "服务器id", width = 15)
    private java.lang.String serverIds;

    /**
     * 已刷新的服务器id，使用,分割
     */
    @Excel(name = "已刷新的服务器id", width = 15)
    private java.lang.String lastServerIds;

    /**
     * 是否自动添加新服
     */
    @Excel(name = "是否自动添加新服", width = 15)
    private Integer autoAddServer;

    /**
     * 活动图标
     */
    @Excel(name = "活动图标", width = 15)
    private java.lang.String icon;

    /**
     * 活动状态
     */
    @Excel(name = "活动状态", width = 15)
    private java.lang.Integer status;

    @Excel(name = "优先级", width = 15)
    private java.lang.Integer priority;

    /**
     * 自动开启
     */
    @Excel(name = "自动开启", width = 15)
    private java.lang.Integer autoOpen;

    /**
     * 活动备注
     */
    @Excel(name = "活动备注", width = 15)
    private java.lang.String remark;

    @TableField(exist = false)
    private List<OpenServiceCampaignType> typeList;

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
