package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @Description: 节日活动-邮件活动-明细
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("game_campaign_type_email_item")
@ApiModel(value = "game_campaign_type_email_item对象", description = "节日活动-邮件活动-明细")
public class GameCampaignTypeEmailItem extends GameCampaignTypeBase {
    private static final long serialVersionUID = 1L;

    /**
     * 活动名称，后台显示
     */
    @Excel(name = "活动名称，后台显示", width = 15)
    @ApiModelProperty(value = "活动名称，后台显示")
    private String name;

    /**
     * 条件类型: 1.任意, 2.全部
     */
    @Excel(name = "条件类型: 1.任意, 2.全部", width = 15)
    @ApiModelProperty(value = "条件类型: 1.任意, 2.全部")
    private Integer conditionType;

    /**
     * 境界
     */
    @Excel(name = "境界", width = 15)
    @ApiModelProperty(value = "境界")
    @TableField(value = "`level`")
    private Integer level;

    /**
     * 剧情关卡
     */
    @Excel(name = "剧情关卡", width = 15)
    @ApiModelProperty(value = "剧情关卡")
    private Integer mainStoryMinorLevel;

    /**
     * 累计登录天数
     */
    @Excel(name = "累计登录天数", width = 15)
    @ApiModelProperty(value = "累计登录天数")
    private Integer loginDay;

    /**
     * 累充统计: 1.注册时间, 2.活动时间
     */
    @Excel(name = "累充统计: 1.注册时间, 2.活动时间", width = 15)
    @ApiModelProperty(value = "累充统计: 1.注册时间, 2.活动时间")
    private Integer rechargeType;

    /**
     * 累充金额
     */
    @Excel(name = "累充金额", width = 15)
    @ApiModelProperty(value = "累充金额")
    private BigDecimal rechargeAmount;

    /**
     * 邮件标题
     */
    @Excel(name = "邮件标题", width = 15)
    @ApiModelProperty(value = "邮件标题")
    private String title;

    /**
     * 邮件描述
     */
    @Excel(name = "邮件描述", width = 15)
    @ApiModelProperty(value = "邮件描述")
    @TableField(value = "`describe`")
    private String describe;

    /**
     * 邮件类型: 1.有附件, 2.冇附件
     */
    @Excel(name = "邮件类型: 1.有附件, 2.冇附件", width = 15)
    @ApiModelProperty(value = "邮件类型: 1.有附件, 2.冇附件")
    @TableField(value = "`type`")
    private Integer type;

    /**
     * 邮件附件
     */
    @Excel(name = "邮件附件", width = 15)
    @ApiModelProperty(value = "邮件附件")
    private String content;
}
