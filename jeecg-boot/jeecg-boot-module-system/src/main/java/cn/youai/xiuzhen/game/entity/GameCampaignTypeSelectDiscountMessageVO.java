/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * <p>
 * 节日活动-自选特惠-传闻部分
 * </p>
 *
 * @author chuantian
 * @since 2021-09-08
 */
@Data

public class GameCampaignTypeSelectDiscountMessageVO {

    /**
     * 推送时间 时分秒
     */
    @ExcelProperty("推送时间 时分秒")
    private String pushTime;

    /**
     * 传闻内容
     */
    @ExcelProperty("传闻内容")
    private String content;

    /**
     * 传闻广播次数
     */
    @ExcelProperty("传闻广播次数")
    private Integer num;

    /**
     * 全服广播邮件标题
     */
    @ExcelProperty("全服广播邮件标题")
    private String emailTitle;

    /**
     * 全服广播邮件内容
     */
    @ExcelProperty("全服广播邮件内容")
    private String emailContent;

}
