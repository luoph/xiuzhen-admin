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
 * @Description: 节日活动-邮件活动-明细
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_email_item")
@ApiModel(value = "game_campaign_type_email_item对象", description = "节日活动-邮件活动-明细")
public class GameCampaignTypeEmailItem extends GameCampaignTypeBaseDetail {
    private static final long serialVersionUID = 1L;

    /**
     * 活动名称，后台显示
     */
    @ExcelProperty("活动名称")
    @Excel(name = "活动名称", width = 15)
    @ApiModelProperty(value = "活动名称")
    private String name;

    /**
     * 条件类型: 1.任意, 2.全部
     */
    @ExcelProperty("条件类型")
    @Excel(name = "条件类型", width = 15)
    @ApiModelProperty(value = "条件类型: 1.任意, 2.全部")
    private Integer conditionType;

    /**
     * 境界
     */
    @ExcelProperty("境界")
    @Excel(name = "境界", width = 15)
    @ApiModelProperty(value = "境界")
    @TableField(value = "`level`")
    private Integer level;

    /**
     * 剧情关卡
     */
    @ExcelProperty("剧情关卡")
    @Excel(name = "剧情关卡", width = 15)
    @ApiModelProperty(value = "剧情关卡")
    private Integer mainStoryMinorLevel;

    /**
     * 累计登录天数
     */
    @ExcelProperty("累计登录天数")
    @Excel(name = "累计登录天数", width = 15)
    @ApiModelProperty(value = "累计登录天数")
    private Integer loginDay;

    /**
     * 累充统计是否判断vip
     */
    @ExcelProperty("累充统计是否判断vip")
    @Excel(name = "累充统计是否判断vip", width = 15)
    @ApiModelProperty(value = "累充统计是否判断vip")
    private Boolean rechargeVip;

    /**
     * 累充统计: 1.注册时间, 2.活动时间
     */
    @ExcelProperty("累充统计")
    @Excel(name = "累充统计", width = 15)
    @ApiModelProperty(value = "累充统计: 1.注册时间, 2.活动时间")
    private Integer rechargeType;

    /**
     * 累充金额
     */
    @ExcelProperty("累充金额")
    @Excel(name = "累充金额", width = 15)
    @ApiModelProperty(value = "累充金额")
    private BigDecimal rechargeAmount;

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
