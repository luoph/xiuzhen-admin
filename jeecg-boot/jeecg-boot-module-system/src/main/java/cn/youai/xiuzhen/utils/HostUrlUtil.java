package cn.youai.xiuzhen.utils;


import org.jeecg.modules.game.entity.GameServer;

/**
 * @ClassName HostUrlUtil
 * @Author buliangliang
 * @Description 游戏服连接
 * @Date 2019/12/25 21:51
 * @Version 1.0
 */
public final class HostUrlUtil {

    /**
     * 获取游戏服的http://gmUrl/param
     *
     * @param gameServer 数据库中服务器实体
     * @param path       param
     * @return http://gmUrl/param
     */
    public static String getHttpUrl(GameServer gameServer, String path) {
        return gameServer.getGmUrl() + path;
    }
}
