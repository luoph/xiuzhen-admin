package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameUpgradeNotice;
import com.baomidou.mybatisplus.extension.service.IService;

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
