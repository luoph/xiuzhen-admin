package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameServerVO;

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
     * @param channelId 渠道 id
     * @return 服务器信息
     */
    List<GameServerVO> selectChannelServerList(Integer channelId);

    /**
     * 通过渠道id查询渠道名称
     *
     * @param channelId 渠道 id
     * @return 渠道名称
     */
    String queryChannelNameById(Integer channelId);

    /**
     * 刷新渠道区服配置
     */
    void updateAllChannelConfig();

    /**
     * 刷新指定渠道的区服配置
     *
     * @param channelId 渠道id
     */
    void updateChannelConfig(Integer channelId);

    /**
     * 刷新ip白名单
     */
    void updateIpWhiteListConfig();
}
