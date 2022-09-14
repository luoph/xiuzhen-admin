package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 设置状态
 *
 * @author luopeihuan
 */
@Getter
public enum SwitchStatus {

    /**
     * 配置状态
     */
    NONE("未设置", -1),
    OFF("关闭", 0),
    ON("开启", 1);

    private final String name;
    private final int value;

    SwitchStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
