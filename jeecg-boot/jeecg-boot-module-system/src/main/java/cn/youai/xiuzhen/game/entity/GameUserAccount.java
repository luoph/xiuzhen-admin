package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 账号
 * @date 2023-01-17
 */
@Data
@Accessors(chain = true)
@TableName("game_user_account")
@ApiModel(value = "GameUserAccount", description = "账号")
public class GameUserAccount {

    /**
     * 自增id 也是玩家注册的自动排序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 帐号
     */
    @Excel(name = "帐号", width = 15)
    @ApiModelProperty(value = "帐号")
    private String account;

    /**
     * 用户类型
     */
    @Excel(name = "用户类型", width = 15)
    @ApiModelProperty(value = "用户类型")
    private String userType;

    /**
     * openid
     */
    @Excel(name = "openid", width = 15)
    @ApiModelProperty(value = "openid")
    private String openId;

    /**
     * 用户名
     */
    @Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 角色数量
     */
    @Excel(name = "角色数量", width = 15)
    @ApiModelProperty(value = "角色数量")
    private Integer roleNum;

    /**
     * 设备id
     */
    @Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    /**
     * 渠道key
     */
    @Excel(name = "渠道", width = 15)
    @ApiModelProperty(value = "渠道")
    private String channel;

    /**
     * Sdk渠道
     */
    @Excel(name = "Sdk渠道", width = 15)
    @ApiModelProperty(value = "Sdk渠道")
    private String sdkChannel;

    /**
     * 来源
     */
    @Excel(name = "来源", width = 15)
    @ApiModelProperty(value = "来源")
    private String source;

    /**
     * 游戏名称
     */
    @Excel(name = "游戏名称", width = 15)
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * sdk名称
     */
    @Excel(name = "sdk名称", width = 15)
    @ApiModelProperty(value = "sdk名称")
    private String sdkName;

    /**
     * sdk版本
     */
    @Excel(name = "sdk版本", width = 15)
    @ApiModelProperty(value = "sdk版本")
    private String sdkVersion;

    /**
     * 身份证号码
     */
    @Excel(name = "身份证号码", width = 15)
    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    /**
     * 生日
     */
    @Excel(name = "生日", width = 15)
    @ApiModelProperty(value = "生日")
    private Date birthday;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 防沉迷帐号分类（4类）
     */
    @Excel(name = "防沉迷帐号分类", width = 15)
    @ApiModelProperty(value = "防沉迷帐号分类")
    private Integer accountType;

    /**
     * 是否已经实名认证
     */
    @Excel(name = "是否实名", width = 15)
    @ApiModelProperty(value = "是否实名")
    private Integer isVerified;

    /**
     * 帐号状态
     * 0 - 无效 1 - 有效
     */
    @Excel(name = "帐号状态", width = 15)
    @ApiModelProperty(value = "帐号状态")
    private Integer status;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 游客在线时长统计的时间
     */
    @Excel(name = "游客在线时长统计的时间", width = 15)
    @ApiModelProperty(value = "游客在线时长统计的时间")
    private Date visitorLimitTime;

    /**
     * 上次登录时间
     */
    @Excel(name = "上次登录时间", width = 15)
    @ApiModelProperty(value = "上次登录时间")
    private Date lastLoginTime;

}
