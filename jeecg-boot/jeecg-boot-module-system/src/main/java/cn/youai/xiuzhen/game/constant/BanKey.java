package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

/**
 * 禁用Key
 *
 * @author luopeihuan
 */
@Getter
public enum BanKey {
    /**
     * 禁用Key
     */
    IP("ip"),
    PLAYER_ID("playerId"),
    DEVICE_ID("deviceId");

    private final String name;

    BanKey(String name) {
        this.name = name;
    }
}