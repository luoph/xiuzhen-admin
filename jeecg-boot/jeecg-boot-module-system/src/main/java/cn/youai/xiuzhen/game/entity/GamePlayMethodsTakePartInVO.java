package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huli
 * @Description: GamePlayMethodsTakePartInVO
 */

@Data
@Accessors(chain = true)
public class GamePlayMethodsTakePartInVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 日期
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @ColumnWidth(15)
    @ExcelProperty("日期")
    @Excel(name = "日期", width = 15)
    private Date date;

    /**
     * xx级登录人数
     */
    @ColumnWidth(15)
    @ExcelProperty("xx级登录人数")
    @Excel(name = "xx级登录人数", width = 15)
    private Integer playerNum;

    /**
     * 参与人数
     */
    @ColumnWidth(15)
    @ExcelProperty("参与人数")
    @Excel(name = "参与人数", width = 15)
    private Integer takePlayInPlayerNum;
    /**
     * 参与率
     */
    @ColumnWidth(15)
    @ExcelProperty("参与率")
    @Excel(name = "参与率", width = 15)
    private BigDecimal takePlayInRate;
    /**
     * 满参与人数
     */
    @ColumnWidth(15)
    @ExcelProperty("满参与人数")
    @Excel(name = "满参与人数", width = 15)
    private Integer allTakePlayInPlayerNum;
    /**
     * 满参率
     */
    @ColumnWidth(15)
    @ExcelProperty("满参率")
    @Excel(name = "满参率", width = 15)
    private BigDecimal allTakePlayInRate;
    /**
     * 回头率
     */
    @ColumnWidth(15)
    @ExcelProperty("回头率")
    @Excel(name = "回头率", width = 15)
    private BigDecimal secondGlanceRate;
}