package org.jeecg.modules.game.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.TimeConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 玩家注册信息表的实体类,player_register_info
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/9 14:42
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerRegisterInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Integer id;

    /**
     *  角色名称
     */
    @Excel(name = "角色名称", width = 15)
    private String name;

    /**
     * 账号
     */
    @Excel(name = "账号", width = 15)
    private String account;

    /**
     * 玩家id
     */
    @Excel(name = "玩家id", width = 15)
    private Integer playerId;

    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = TimeConstant.DEFAULT_DATE_FORMAT)
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_TIME_FORMAT)
    private Date createDate;

    /**
     * 充值预警天数
     */
    @Excel(name = "充值预警天数", width = 15)
    private  int payWarningDays;

    /**
     * 充值预警天数
     */
    @Excel(name = "登录预警天数", width = 15)
    private  int loginWarningDays;

}
