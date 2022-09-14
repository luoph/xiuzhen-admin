package cn.youai.xiuzhen.game.model;

import cn.youai.xiuzhen.game.entity.GameServerTag;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private List<GameServerTag> tagList = new ArrayList<>();

    public ChannelConfig(Long noticeId, UpdateConfig updateConfig, List<GameServerTag> tagList, List<GameServerVO> serverList, boolean taStatistics) {
        this.noticeId = noticeId;
        this.updateConfig = updateConfig;
        this.serverList.addAll(serverList);
        this.tagList.addAll(tagList);
        this.taStatistics = taStatistics;
    }

    public static ChannelConfig of(Long noticeId, UpdateConfig updateConfig, List<GameServerTag> tagList, List<GameServerVO> serverList, boolean taStatistics) {
        return new ChannelConfig(noticeId, updateConfig, tagList, serverList, taStatistics);
    }
}
