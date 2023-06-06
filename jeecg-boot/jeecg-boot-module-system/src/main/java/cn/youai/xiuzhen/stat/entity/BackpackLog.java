package cn.youai.xiuzhen.stat.entity;

import cn.youai.xiuzhen.core.base.IPlayerData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 背包道具操作日志记录
 *
 * @author ocean
 * @since 202109131547
 */
@Data
@Document(collection = "backpack_log")
public class BackpackLog implements IPlayerData, Serializable {

    private static final long serialVersionUID = 7925160499013709552L;

    /**
     * 自增id
     */
    @Id
    @Excel(name = "id", width = 15)
    private String _id;

    @Excel(name = "玩家id", width = 15)
    private Long playerId;

    @Excel(name = "昵称", width = 15)
    private String nickname;

    @Excel(name = "区服id", width = 15)
    private Integer serverId;

    @Excel(name = "创角区服", width = 15)
    private Integer sid;

    @Excel(name = "道具id", width = 15)
    private Integer itemId;

    @Excel(name = "道具名", width = 15)
    private String itemName;

    @Excel(name = "数量", width = 15)
    private Long num;

    /**
     * 1存入 2消耗
     */
    @Excel(name = "方式", width = 15)
    private Integer type;

    /**
     * 获取途径
     */
    @Excel(name = "途径", width = 15)
    private Integer way;

    /**
     * 途径多选
     */
    @Excel(name = "途径多选", width = 15)
    private String ways;

    @Excel(name = "途径名", width = 15)
    private String wayName;

    @Excel(name = "更新前数量", width = 15)
    private Long beforeNum;

    @Excel(name = "更新后数量", width = 15)
    private Long afterNum;

    @Excel(name = "创建日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date createDate;

    @Excel(name = "创建时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date createTime;
}
