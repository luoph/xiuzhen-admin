package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.*;
import cn.youai.xiuzhen.game.mapper.GameChannelServerMapper;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IOpenServiceCampaignService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道服配置
 * @date 2019-12-11
 */
@Service
@DS("master")
public class GameChannelServerServiceImpl extends ServiceImpl<GameChannelServerMapper, GameChannelServer> implements IGameChannelServerService {

    @Autowired
    private IGameCampaignService gameCampaignService;

    @Autowired
    private IOpenServiceCampaignService openServiceCampaignService;

    @Autowired
    private IGameChannelService gameChannelService;

    @Override
    public int selectLastPosition(int channelId) {
        return getBaseMapper().selectLastPosition(channelId);
    }

    @Async
    @Override
    public void autoAddCampaignServerIds(List<GameChannelServer> list) {
        // setChannelSimpleName
        Set<Integer> channelIds = CollUtil.isNotEmpty(list) ? list.stream().map(GameChannelServer::getChannelId).collect(Collectors.toSet()) : Collections.emptySet();
        List<GameChannel> channelList = gameChannelService.selectChannelList(channelIds);
        Map<Integer, GameChannel> channelMap = CollUtil.isNotEmpty(channelList) ? channelList.stream()
                .collect(Collectors.toMap(GameChannel::getId, Function.identity(), (key1, key2) -> key2)) : Collections.emptyMap();
        list.forEach(e -> {
            GameChannel channel = channelMap.get(e.getChannelId());
            if (null != channel) {
                e.setChannelSimpleName(channel.getSimpleName());
            }
        });

        gameCampaignService.addCampaignServerIds(list);
        openServiceCampaignService.addCampaignServerIds(list);
    }

    @Override
    public List<GameServerVO> selectServerListByChannelId(int channelId) {
        return getBaseMapper().selectServerListByChannelId(channelId);
    }

    @Override
    public List<GameChannel> selectChannelList(String channel) {
        return getBaseMapper().selectChannelList(channel);
    }

    @Override
    public List<GameSdkChannel> selectSdkChannelList(Collection<String> channels, String sdkChannel) {
        return getBaseMapper().selectSdkChannelList(channels, sdkChannel);
    }

    @Override
    public List<GameServer> selectServerList(Collection<String> channels) {
        return getBaseMapper().selectChannelServerList(channels);
    }

    @Override
    public List<GameServerVO> filterServerList(String channel, List<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return new ArrayList<>();
        }
        return getBaseMapper().filterServerList(channel, serverIds);
    }

}