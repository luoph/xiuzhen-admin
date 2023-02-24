package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.utils.StringUtils;
import cn.youai.enums.OutdatedType;
import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServerTag;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import cn.youai.xiuzhen.game.mapper.GameChannelMapper;
import cn.youai.xiuzhen.game.model.ChannelConfig;
import cn.youai.xiuzhen.game.model.UpdateConfig;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerTagService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.JsonFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
@Service
@DS("master")
public class GameChannelServiceImpl extends ServiceImpl<GameChannelMapper, GameChannel> implements IGameChannelService {

    @Value("${app.folder.server}")
    private String serverFolder;

    @Value("${app.folder.ip-whitelist}")
    private String ipWhitelistFolder;

    @Autowired
    private IGameServerTagService serverTagService;

    @Override
    public GameChannel selectChannel(String channel) {
        return getOne(QueryUtils.safeSelectOneQuery(Wrappers.<GameChannel>lambdaQuery().eq(GameChannel::getSimpleName, channel)));
    }

    @Override
    public List<GameChannel> selectChannelList(Collection<Integer> channelIds) {
        return list(Wrappers.<GameChannel>lambdaQuery().in(GameChannel::getId, channelIds));
    }

    @Override
    public List<GameServerVO> selectChannelServerList(int channelId) {
        return getBaseMapper().selectChannelServerList(channelId);
    }

    @Override
    public String queryChannelNameById(int channelId) {
        return getBaseMapper().queryChannelNameById(channelId);
    }

    @Override
    public void updateAllChannelConfig() {
        List<GameChannel> channelList = list();
        if (CollUtil.isNotEmpty(channelList)) {
            for (GameChannel channel : channelList) {
                updateChannelServerJson(channel);
            }
        }
    }

    @Override
    public void updateChannelConfig(int channelId) {
        GameChannel gameChannel = getById(channelId);
        if (gameChannel != null) {
            updateChannelServerJson(gameChannel);
        }
    }

    @Override
    public void updateIpWhiteListConfig() {
        List<GameChannel> channelList = list();
        if (CollUtil.isEmpty(channelList)) {
            return;
        }

        for (GameChannel channel : channelList) {
            if (StringUtils.isNotBlank(channel.getIpWhitelist())) {
                Set<String> ipList = StringUtils.split2Set(channel.getIpWhitelist());
                JsonFileUtils.writeJsonFile(ipList, ipWhitelistFolder, channel.getSimpleName());
            }
        }
    }

    private void updateChannelServerJson(GameChannel channel) {
        List<GameServerVO> servers = selectChannelServerList(channel.getId());
        servers = servers.stream().filter(t -> t.getOutdated() != OutdatedType.OFFLINE.getValue())
                .collect(Collectors.toList());
        UpdateConfig updateConfig = new UpdateConfig()
                .setVersionCode(channel.getVersionCode())
                .setVersionName(channel.getVersionName())
                .setVersionUpdateTime(channel.getVersionUpdateTime());

        List<GameServerTag> tagList = serverTagService.selectTags();
        boolean enableTa = channel.getTaStatistics() != null && channel.getTaStatistics() == 1;
        JsonFileUtils.writeJsonFile(ChannelConfig.of(channel.getNoticeId(), updateConfig, tagList, servers, enableTa),
                serverFolder, channel.getSimpleName());
    }
}
