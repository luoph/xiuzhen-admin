package cn.youai.xiuzhen.game.entity;

import cn.youai.xiuzhen.game.monitor.DiskUsageInfo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 虚拟主机
 * @date 2023-04-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_vps")
@ApiModel(value = "GameVps", description = "虚拟主机")
public class GameVps extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 游戏名称
     */
    @Excel(name = "名称", width = 15)
    private String name;
    /**
     * hostname
     */
    @Excel(name = "hostname", width = 15)
    private String hostname;
    /**
     * 公网地址
     */
    @Excel(name = "ip", width = 15)
    private String ip;

    /**
     * 内网地址
     */
    @Excel(name = "lan", width = 15)
    private String lan;

    /**
     * 系统
     */
    @Excel(name = "os", width = 15)
    private String os;

    /**
     * 游戏服数量
     */
    @TableField(exist = false)
    @Excel(name = "游戏服数量", width = 15)
    private Integer gameServerNum;
    /**
     * 游戏服Id
     */
    @TableField(exist = false)
    @Excel(name = "游戏服Id", width = 15)
    private String gameServerIds;

    /**
     * 跨服数量
     */
    @TableField(exist = false)
    @Excel(name = "跨服数量", width = 15)
    private Integer crossServerNum;

    /**
     * 跨服Id
     */
    @TableField(exist = false)
    @Excel(name = "跨服Id", width = 15)
    private String crossServerIds;

    @TableField(exist = false)
    @Excel(name = "平台", width = 15)
    private String platform;

    @TableField(exist = false)
    @Excel(name = "总内存", width = 15)
    private String totalMem;

    @TableField(exist = false)
    @Excel(name = "CPU核心数", width = 15)
    private Integer cpuCoreNum;

    @TableField(exist = false)
    @Excel(name = "CPU使用率", width = 15)
    private BigDecimal cpuPer;

    @TableField(exist = false)
    @Excel(name = "内存使用率", width = 15)
    private BigDecimal memPer;

    @TableField(exist = false)
    @Excel(name = "5分钟负载", width = 15)
    private BigDecimal fiveLoad;

    @TableField(exist = false)
    @Excel(name = "15分钟负载", width = 15)
    private BigDecimal fifteenLoad;

    @TableField(exist = false)
    @Excel(name = "启动时间", width = 15)
    private Date bootTime;

    @TableField(exist = false)
    @Excel(name = "上传时间", width = 15)
    private Date uploadTime;

    @TableField(exist = false)
    @Excel(name = "磁盘占用", width = 15)
    private List<DiskUsageInfo> diskList;

    @TableField(exist = false)
    @Excel(name = "在线玩家", width = 15)
    private Integer onlineNum;
}
