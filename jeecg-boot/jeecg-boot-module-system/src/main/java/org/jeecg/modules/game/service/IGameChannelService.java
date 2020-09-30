package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameServer;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
public interface IGameChannelService extends IService<GameChannel> {

    /**
     * 查询某个渠道的服务器
     *
     * @param channelId
     * @return
     */
    List<GameServer> getServerListChannelId(Long channelId);

    /**
     * 通过渠道id查询渠道名称
     *
     * @param channelId
     * @return
     */
    String queryChannelNameById(Integer channelId);
}
