package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
public interface IGameChannelServerService extends IService<GameChannelServer> {

    void autoAddCampaignServerIds(List<GameChannelServer> channelServers);

    /**
     * 通过渠道id查询对应的服务器
     *
     * @param channelId 渠道id
     * @return 区服列表
     */
    List<GameServerVO> selectServerListByChannelId(int channelId);

    List<GameChannel> selectChannelList();

    List<GameServer> selectServerList(Collection<String> channels);

    List<GameSdkChannel> selectSdkChannelList(Collection<String> channels);

    List<GameServerVO> filterServerList(String channel, List<Integer> serverIds);

}
