package org.jeecg.modules.game.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.modules.game.entity.GameServerVO;

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
    // 是否上传数数统计
    private boolean taStatistics;
    private List<GameServerVO> serverList = new ArrayList<>();

    public ChannelConfig(Long noticeId, UpdateConfig updateConfig, List<GameServerVO> serverList, boolean taStatistics) {
        this.noticeId = noticeId;
        this.updateConfig = updateConfig;
        this.serverList.addAll(serverList);
        this.taStatistics = taStatistics;
    }

    public static ChannelConfig of(Long noticeId, UpdateConfig updateConfig, List<GameServerVO> serverList, boolean taStatistics) {
        return new ChannelConfig(noticeId, updateConfig, serverList, taStatistics);
    }
}
