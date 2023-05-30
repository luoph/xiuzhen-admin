package cn.youai.xiuzhen.game.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * 服务器版本类型: 1.普通服, 2.BT服
 *
 * @author ocean
 * @since 202305301149
 */
@Getter
public enum GameServerVersionType {

    /**
     * 服务器版本类型: 1.普通服, 2.BT服
     */
    NORMAL(1),
    BT(2);

    private final int type;

    GameServerVersionType(int type) {
        this.type = type;
    }

    public static GameServerVersionType valueOf(int type) {
        return Arrays.stream(GameServerVersionType.values()).filter(e -> e.type == type).findFirst().orElse(null);
    }
}
