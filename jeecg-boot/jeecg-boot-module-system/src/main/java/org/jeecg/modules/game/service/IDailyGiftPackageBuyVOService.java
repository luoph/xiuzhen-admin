package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.DailyGiftPackageBuyVO;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description daily_gift_package_buy
 * @date 2020-09-30
 */
public interface IDailyGiftPackageBuyVOService {

    /**
     * 通过创建时间查询消费礼包
     * @param serverId
     * @param createTimeBegin
     * @param createTimeEnd
     * @return
     */
    List<DailyGiftPackageBuyVO> queryGiftPackageByDateRange(Integer serverId, String createTimeBegin, String createTimeEnd);
}
