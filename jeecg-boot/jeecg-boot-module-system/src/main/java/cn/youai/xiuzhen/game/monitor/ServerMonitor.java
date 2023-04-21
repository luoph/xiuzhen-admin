package cn.youai.xiuzhen.game.monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 磁盘使用信息
 *
 * @author luopeihuan
 * @version 1.0
 * @date 2023/04/14
 */
@Data
@Accessors(chain = true)
public class ServerMonitor {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    @TableField(exist = false)
    @Excel(name = "hostname", width = 15)
    private String hostname;

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
    private String diskUsage;
}
