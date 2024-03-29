package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 激活码
 * @date 2020-01-07
 */
@Data
@TableName("game_redeem_code")
public class GameRedeemCode implements Serializable {

    private static final long serialVersionUID = 1L;

    public GameRedeemCode() {
    }

    public GameRedeemCode(Integer activityId, String code, Integer totalNum, Integer usedNum, Integer status) {
        this.activityId = activityId;
        this.code = code;
        this.totalNum = totalNum;
        this.usedNum = usedNum;
        this.status = status;
    }

    /**
     * id
     */
    @Excel(name = "id", width = 15)
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 激活码活动id
     */
    @Excel(name = "激活码活动id", width = 15)
    private java.lang.Integer activityId;

    /**
     * 激活码
     */
    @Excel(name = "激活码", width = 15)
    private java.lang.String code;

    /**
     * 批量生成的激活码数量
     */
    @ExcelIgnore
    @TableField(exist = false)
    private java.lang.Integer batchNum;

    /**
     * 可使用总数
     */
    @Excel(name = "可使用总数", width = 15)
    private java.lang.Integer totalNum;

    /**
     * 已使用数量
     */
    @Excel(name = "已使用数量", width = 15)
    private java.lang.Integer usedNum;

    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    private java.lang.Integer status;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 15, format = TimeConstant.DEFAULT_TIME_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private java.util.Date updateTime;
}
