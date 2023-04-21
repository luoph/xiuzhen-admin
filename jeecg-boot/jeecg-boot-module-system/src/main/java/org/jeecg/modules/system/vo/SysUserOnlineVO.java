package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.TimeConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: chenli
 * @Date: 2020-06-07
 * @Version: V1.0
 */
@Data
public class SysUserOnlineVO {
    /**
     * 会话id
     */
    private String id;

    /**
     * 会话编号
     */
    private String token;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户名
     */
    private String realname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = TimeConstant.DEFAULT_TIMEZONE, pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    @DateTimeFormat(pattern = TimeConstant.DEFAULT_DATE_FORMAT)
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Dict(dicCode = "sex")
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;
}
