package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.entity.GameServerVO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Async
    @Override
    public void autoAddCampaignServerIds(List<GameChannelServer> channelServers) {
        // setChannelSimpleName
        Set<Integer> channelIds = channelServers.stream().map(e -> Integer.parseInt(e.getChannelId())).collect(Collectors.toSet());
        Map<Integer, GameChannel> channelMap = gameChannelService.selectChannelList(channelIds)
                .stream().collect(Collectors.toMap(GameChannel::getId, Function.identity(), (key1, key2) -> key2));
        channelServers.forEach(e -> {
            GameChannel gameChannel = channelMap.get(Integer.parseInt(e.getChannelId()));
            if (null != gameChannel) {
                e.setChannelSimpleName(gameChannel.getSimpleName());
            }
        });

        gameCampaignService.addCampaignServerIds(channelServers);
        openServiceCampaignService.addCampaignServerIds(channelServers);
    }

    @Override
    public List<GameServerVO> selectServerListByChannelId(int channelId) {
        return getBaseMapper().selectServerListByChannelId(channelId);
    }

    @Override
    public List<GameServerVO> selectServerList(String channel) {
        if (StringUtils.isBlank(channel)) {
            return getBaseMapper().selectServerList();
        }
        return getBaseMapper().selectChannelServerList(channel);
    }

    @Override
    public List<GameChannel> selectChannelList() {
        return getBaseMapper().selectChannelList();
    }

    @Override
    public List<GameServerVO> filterServerList(String channel, List<Integer> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return new ArrayList<>();
        }
        return getBaseMapper().filterServerList(channel, serverIds);
    }

}