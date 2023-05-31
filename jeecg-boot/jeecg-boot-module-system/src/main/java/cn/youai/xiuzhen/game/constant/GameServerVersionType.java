package cn.youai.xiuzhen.game.constant;

import cn.youai.xiuzhen.game.cache.GameSettingCache;
import cn.youai.xiuzhen.game.entity.GameSetting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.function.ToLongFunction;

/**
 * 服务器版本类型: 1.普通服, 2.BT服
 *
 * @author ocean
 * @since 202305301149
 */
@Slf4j
@Getter
public enum GameServerVersionType {

    /**
     * 服务器版本类型: 1.普通服, 2.BT服
     */
    NORMAL(1, 1002, GameServerVersionType::getNormalRefundNum),
    BT(2, 1217, GameServerVersionType::getBtRefundNum);

    private final int type;
    private final int itemId;
    private final ToLongFunction<Double> function;

    GameServerVersionType(int type, int itemId, ToLongFunction<Double> function) {
        this.type = type;
        this.itemId = itemId;
        this.function = function;
    }

    public static GameServerVersionType valueOf(int type) {
        return Arrays.stream(GameServerVersionType.values()).filter(e -> e.type == type).findFirst().orElse(null);
    }

    /**
     * 普通服: 1000以内按总金额的1200%换算，超出部分按1500%换算
     *
     * @param totalAmount 总充值金额
     * @return 返还仙玉数量
     */
    private static long getNormalRefundNum(double totalAmount) {
        double amountLe1000 = Math.min(totalAmount, 1000);
        long num = (long) (amountLe1000 * 12);
        double amountGt1000 = totalAmount - 1000;
        if (amountGt1000 > 0) {
            num += amountGt1000 * 15;
        }
        return num;
    }

    private static long getBtRefundNum(double totalAmount) {
        GameSetting gameSetting = GameSettingCache.getInstance().get(GameSettingKey.BT_STOP_SERVER_REFUND_RATIO);
        if (null == gameSetting) {
            log.error("getBtRefundNum() error, gameSetting is null. key={}", GameSettingKey.BT_STOP_SERVER_REFUND_RATIO);
            return 0;
        }
        return (long) (Double.parseDouble(gameSetting.getDictValue()) * 0.01 * totalAmount);
    }

}
