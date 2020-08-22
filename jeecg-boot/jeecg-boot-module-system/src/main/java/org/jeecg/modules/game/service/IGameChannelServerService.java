package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameServer;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
public interface IGameChannelServerService extends IService<GameChannelServer> {
    /**
     * 通过渠道id查询对应的服务器
     *
     * @param channelId
     * @return
     */
    List<GameServer> gameServerByChannelId(int channelId);

    /**
     * 渠道和服务器绑定关系验证
     *
     * @param channelId
     * @param serverId
     * @return
     */
    boolean isValidChannelWithServer(int channelId, int serverId);
}
