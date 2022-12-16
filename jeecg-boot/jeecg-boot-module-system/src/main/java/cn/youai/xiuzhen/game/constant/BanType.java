package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 禁用类型
 *
 * @author luopeihuan
 */
@Getter
public enum BanType {
    /**
     * 帐号
     */
    LOGIN(1),

    CHAT(2);

    private final int value;

    BanType(int value) {
        this.value = value;
    }
}
