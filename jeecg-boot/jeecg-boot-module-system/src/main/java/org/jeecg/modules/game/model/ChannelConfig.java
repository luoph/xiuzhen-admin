package org.jeecg.modules.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.modules.game.entity.GameServer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luopeihuan
 * @version 1.0
 * @date 2019-12-21.
 */
@Data
@Accessors(chain = true)
public class ChannelConfig {
    private Long noticeId;
    private UpdateConfig updateConfig;
    private List<GameServer> serverList = new ArrayList<>();

    public ChannelConfig(Long noticeId, UpdateConfig updateConfig, List<GameServer> serverList) {
        this.noticeId = noticeId;
        this.updateConfig = updateConfig;
        this.serverList.addAll(serverList);
    }
}
