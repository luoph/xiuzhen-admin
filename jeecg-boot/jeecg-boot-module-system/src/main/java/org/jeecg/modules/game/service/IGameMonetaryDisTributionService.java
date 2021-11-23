package org.jeecg.modules.game.service;

import cn.youai.basics.model.DateRange;
import org.jeecg.modules.game.entity.MonetaryDisTributionVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huli
 * @Description: GameMonetaryDisTributionService
 * @date 2020/12/29 10:34
 */

public interface IGameMonetaryDisTributionService {
    /**
     * @param channelId
     * @param serverId
     * @param rangeTimeBegin
     * @param rangeTimeEnd
     * @param productAndMarketTyep 产销类型：1：产出、2：消耗
     * @param quantityType         货币类型：1010："仙石"、1002： "玉髓"、1001："灵石"
     * @return
     */
    List<MonetaryDisTributionVO> monetaryDistributionList(int channelId, int serverId, String rangeTimeBegin, String rangeTimeEnd, int productAndMarketTyep, int quantityType);

    Map<Date, List<MonetaryDisTributionVO>> monetaryDistributionList(int channelId, int serverId, DateRange dateRange, int productAndMarketType, int quantityType);
}