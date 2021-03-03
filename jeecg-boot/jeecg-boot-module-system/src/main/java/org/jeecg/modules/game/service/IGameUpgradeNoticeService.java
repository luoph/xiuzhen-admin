package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameUpgradeNotice;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 更新公告
 * @date 2021-03-02
 */
public interface IGameUpgradeNoticeService extends IService<GameUpgradeNotice> {
    /**
     * 同步服务器
     *
     * @param gameUpgradeNotice 实体
     */
    void syncServerAll(GameUpgradeNotice gameUpgradeNotice);
}
