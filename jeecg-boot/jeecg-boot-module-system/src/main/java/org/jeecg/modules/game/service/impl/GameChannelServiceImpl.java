package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.JsonFileUtils;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameServerVO;
import org.jeecg.modules.game.mapper.GameChannelMapper;
import org.jeecg.modules.game.model.ChannelConfig;
import org.jeecg.modules.game.model.UpdateConfig;
import org.jeecg.modules.game.service.IGameChannelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏渠道
 * @date 2019-12-11
 */
@Service
public class GameChannelServiceImpl extends ServiceImpl<GameChannelMapper, GameChannel> implements IGameChannelService {

    @Value("${app.folder.server}")
    private String serverFolder;

    @Value("${app.folder.ip-whitelist}")
    private String ipWhitelistFolder;

    @Override
    public List<GameServerVO> selectServerListByChannelId(Integer channelId) {
        return getBaseMapper().selectServerListByChannelId(channelId);
    }

    @Override
    public String queryChannelNameById(Integer channelId) {
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
    public void updateChannelConfig(Integer channelId) {
        GameChannel gameChannel = getById(channelId);
        if (gameChannel != null) {
            updateChannelServerJson(gameChannel);
        }
    }

    @Override
    public void updateIpWhiteListConfig() {
        List<GameChannel> channelList = list();
        if (CollUtil.isNotEmpty(channelList)) {
            for (GameChannel channel : channelList) {
                if (StringUtils.isNotBlank(channel.getIpWhitelist())) {
                    String[] array = channel.getIpWhitelist().split(",");
                    List<String> ipList = new ArrayList<>();
                    for (String s : array) {
                        ipList.add(s.trim());
                    }

                    JsonFileUtils.writeJsonFile(ipList, ipWhitelistFolder, channel.getSimpleName());
                }
            }
        }
    }

    private void updateChannelServerJson(GameChannel channel) {
        List<GameServerVO> servers = selectServerListByChannelId(channel.getId());
        UpdateConfig updateConfig = new UpdateConfig()
                .setVersionCode(channel.getVersionCode())
                .setVersionName(channel.getVersionName())
                .setVersionUpdateTime(channel.getVersionUpdateTime());

        JsonFileUtils.writeJsonFile(new ChannelConfig(channel.getNoticeId(), updateConfig, servers),
                serverFolder, channel.getSimpleName());
    }
}
