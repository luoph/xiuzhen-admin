package org.jeecg.modules.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @author huli
 * @Description: MilitaryStrengthVO
 * @date 2021/1/4 11:26
 */

@Data
@TableName("表名（如果不设置，那么会以类名以驼峰形式对于表）")
@Accessors(chain = true)
public class MilitaryStrengthVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 玩家id
     */
    @ColumnWidth(15)
    @ExcelProperty("玩家id")
    @Excel(name = "玩家id", width = 15)
    private String userId;

    /**
     * 玩家名
     */
    @ColumnWidth(15)
    @ExcelProperty("玩家名")
    @Excel(name = "玩家名", width = 15)
    private String userName;

    /**
     * 战力变更
     */
    @ColumnWidth(15)
    @ExcelProperty("战力变更")
    @Excel(name = "战力变更", width = 15)
    private String militaryStrengthChange;

    /**
     * 原战力
     */

    @ColumnWidth(15)
    @ExcelProperty("原战力")
    @Excel(name = "原战力", width = 15)
    private String originalMilitary;

    /**
     * 新战力
     */
    @ColumnWidth(15)
    @ExcelProperty("新战力")
    @Excel(name = "新战力", width = 15)
    private String newMilitary;

    /**
     * 操作
     */
    @ColumnWidth(15)
    @ExcelProperty("操作")
    @Excel(name = "操作", width = 15)
    private String operation;

    /**
     * 时间
     */
    @ColumnWidth(15)
    @ExcelProperty("时间")
    @Excel(name = "时间", width = 15)
    private String time;


}