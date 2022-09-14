package cn.youai.xiuzhen.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 客户端更新配置
 * @date 2021-06-10
 */
@Data
@Accessors(chain = true)
@TableName("game_app_update")
@EqualsAndHashCode(callSuper = true)
public class GameAppUpdate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private java.lang.Long id;

    /**
     * 游戏id
     */
    @Excel(name = "游戏id", width = 15)
    private java.lang.Integer gameId;

    /**
     * apk名称
     */
    @Excel(name = "名称", width = 15)
    private java.lang.String appName;

    /**
     * 应用包名
     */
    @Excel(name = "应用包名", width = 15)
    private java.lang.String packageName;

    /**
     * 版本号
     */
    @Excel(name = "版本号", width = 15)
    private java.lang.Integer versionCode;

    /**
     * 版本名
     */
    @Excel(name = "版本名", width = 15)
    private java.lang.String versionName;

    /**
     * 平台：ios/android
     */
    @Excel(name = "平台", width = 15)
    private java.lang.String platform;

    /**
     * 包渠道
     */
    @Excel(name = "包渠道", width = 15)
    private java.lang.String channel;

    /**
     * 下载地址
     */
    @Excel(name = "下载地址", width = 15)
    private java.lang.String downloadUrl;

    /**
     * 更新标题
     */
    @Excel(name = "更新标题", width = 15)
    private java.lang.String updateTitle;

    /**
     * 更新内容
     */
    @Excel(name = "更新内容", width = 15)
    private java.lang.String updateContent;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;

}
