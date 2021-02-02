package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @date 2021-02-02
 */
@Data
public class GameCampaignTypeReduceVO {

    /**
     * 排名序列
     */
    @ExcelProperty("排名序列")
    private Integer sort;

    /**
     * 消耗道具id,item_id
     */
    @ExcelProperty("消耗道具id,item_id")
    private Integer reduceItemId;

    /**
     * 上榜下限数量
     */
    @ExcelProperty("上榜下限数量")
    private Integer limitNum;

    /**
     * 奖励内容[{"itemId":1001,"num":100}]
     */
    @ExcelProperty("奖励内容")
    private String reward;

}
