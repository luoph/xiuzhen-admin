package org.jeecg.modules.player.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.PlayerItemLog;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
public interface IPlayerItemLogService extends IService<PlayerItemLog> {

    /**
     * 设置日志属性
     *
     * @param serverId
     * @param backpacklog
     */
    PlayerItemLog writePlayerItemLog(long serverId, BackpackLog backpacklog);

    /**
     * 保存日志
     *
     * @param logs
     */
    void saveBatchLog(List<PlayerItemLog> logs);
}
