package cn.youai.xiuzhen.game.constant;

import cn.youai.xiuzhen.game.entity.GameServer;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GameServerUtils {

    public static <T> void apply(List<GameServer> list, GameServer src, Function<GameServer, T> getter, BiFunction<GameServer, T, GameServer> setter) {
        T value = getter.apply(src);
        list.forEach(t -> setter.apply(t, value));
    }

    public static <T> boolean equal(GameServer from, GameServer to, Function<GameServer, T> getter) {
        T fromValue = getter.apply(from);
        T toValue = getter.apply(to);
        return Objects.equals(fromValue, toValue);
    }

    public static <T> boolean notEq(GameServer from, GameServer to, Function<GameServer, T> getter) {
        return !equal(from, to, getter);
    }

}
