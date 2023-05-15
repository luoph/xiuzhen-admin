package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @Description: 单日仙玉返利
 * @Author: jeecg-boot
 * @Date: 2023-05-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("game_campaign_type_single_day_recharge_jade_rebate")
@ApiModel(value = "game_campaign_type_single_day_recharge_jade_rebate对象", description = "单日仙玉返利")
public class GameCampaignTypeSingleDayRechargeJadeRebate extends GameCampaignTypeBaseDetail {
    private static final long serialVersionUID = 1L;

    /**
     * 活动名称，后台显示
     */
    @Excel(name = "活动名称，后台显示", width = 15)
    @ApiModelProperty(value = "活动名称，后台显示")
    private String name;

    /**
     * 最小累充金额
     */
    @Excel(name = "最小累充金额", width = 15)
    @ApiModelProperty(value = "最小累充金额")
    private BigDecimal minRechargeAmount;

    /**
     * 最大累充金额
     */
    @Excel(name = "最大累充金额", width = 15)
    @ApiModelProperty(value = "最大累充金额")
    private BigDecimal maxRechargeAmount;

    /**
     * 返利比例
     */
    @Excel(name = "返利比例", width = 15)
    @ApiModelProperty(value = "返利比例")
    private BigDecimal rebatePct;

    /**
     * 邮件标题
     */
    @ExcelProperty("邮件标题")
    @Excel(name = "邮件标题", width = 15)
    @ApiModelProperty(value = "邮件标题")
    private String title;

    /**
     * 邮件描述
     */
    @ExcelProperty("邮件描述")
    @Excel(name = "邮件描述", width = 15)
    @ApiModelProperty(value = "邮件描述")
    @TableField(value = "`describe`")
    private String describe;

    /**
     * 下一档邮件描述
     */
    @ExcelProperty("下一档邮件描述")
    @Excel(name = "下一档邮件描述", width = 15)
    @ApiModelProperty(value = "下一档邮件描述")
    private String nextDescribe;

    /**
     * 邮件类型: 1.有附件, 2.冇附件
     */
    @ExcelProperty("邮件类型")
    @Excel(name = "邮件类型", width = 15)
    @ApiModelProperty(value = "邮件类型: 1.有附件, 2.冇附件")
    @TableField(value = "`type`")
    private Integer type;

    /**
     * 邮件附件
     */
    @ExcelProperty("邮件附件")
    @Excel(name = "邮件附件", width = 15)
    @ApiModelProperty(value = "邮件附件")
    private String content;
}
