package org.jeecg.modules.game.model;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.modules.game.entity.GameAppUpdate;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-21.
 */
@Data
@Accessors(chain = true)
public class AppConfig {

    public AppConfig() {
    }

    public static AppConfig valueOf(GameAppUpdate appUpdate) {
        AppConfig appConfig = new AppConfig();
        BeanUtil.copyProperties(appUpdate, appConfig, true);
        return appConfig;
    }

    /**
     * apk名称
     */
    private String appName;

    /**
     * 版本号
     */
    private Integer versionCode;

    /**
     * 版本名
     */
    private String versionName;

    /**
     * 应用包名
     */
    private String packageName;

    /**
     * 平台：ios/android
     */
    private String platform;

    /**
     * 包渠道
     */
    private String channel;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 更新标题
     */
    private String updateTitle;

    /**
     * 更新内容
     */
    private String updateContent;
}
