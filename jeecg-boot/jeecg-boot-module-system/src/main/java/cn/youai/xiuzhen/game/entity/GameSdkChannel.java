package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏SDK渠道
 * @date 2023-04-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_sdk_channel")
@ApiModel(value = "GameSdkChannel", description = "游戏SDK渠道")
public class GameSdkChannel extends BaseEntity {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 渠道名称
     */
    @Excel(name = "名称", width = 15)
    private String name;

    /**
     * 渠道
     */
    @Excel(name = "渠道", width = 15)
    private String channel;

    /**
     * SDK渠道
     */
    @Excel(name = "SDK渠道", width = 15)
    private String sdkChannel;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private String remark;

    /**
     * 上线时间
     */
    @Excel(name = "上线时间", width = 20, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date onlineTime;
}
