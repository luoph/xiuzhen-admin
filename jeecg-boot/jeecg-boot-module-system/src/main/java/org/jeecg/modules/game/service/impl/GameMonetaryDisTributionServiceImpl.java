package org.jeecg.modules.game.service.impl;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.constant.FairyJadeBuyType;
import org.jeecg.modules.game.constant.ItemId;
import org.jeecg.modules.game.entity.MonetaryDisTributionVO;
import org.jeecg.modules.game.mapper.GameMonetaryDisTributionMapper;
import org.jeecg.modules.game.service.IGameMonetaryDisTributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.jeecg.modules.game.constant.FairyJadeBuyType.*;

/**
 * @author 胡立
 * @Description: GameMonetaryDisTributionServiceImpl
 * @date 2020/12/29 10:40
 */

@Service
public class GameMonetaryDisTributionServiceImpl implements IGameMonetaryDisTributionService {
    Log log = LogFactory.getLog(this.getClass());
    /**
     * 产销点类型map
     */
    private static final Map<Integer,String> FairyJadeBuyTypeMap = new HashMap<>();
    static {
        FairyJadeBuyTypeMap.put(FairyJadeBuyType.PRACTICE_FUND.getType(),FairyJadeBuyType.PRACTICE_FUND.getDesc());
        FairyJadeBuyTypeMap.put(FairyJadeBuyType.IMMORTAL.getType(),FairyJadeBuyType.IMMORTAL.getDesc());
        FairyJadeBuyTypeMap.put(FairyJadeBuyType.DAILY_GIFT.getType(),FairyJadeBuyType.DAILY_GIFT.getDesc());
        FairyJadeBuyTypeMap.put(FairyJadeBuyType.SEVEN_DAY_GIFT.getType(),FairyJadeBuyType.SEVEN_DAY_GIFT.getDesc());
        FairyJadeBuyTypeMap.put(FairyJadeBuyType.ZERO_BUY.getType(),FairyJadeBuyType.ZERO_BUY.getDesc());
    }

    @Resource
    private GameMonetaryDisTributionMapper gameMonetaryDisTributionMapper;
    @Override
    public List<MonetaryDisTributionVO> monetaryDistributionList(int channelId, int serverId, String rangeTimeBegin, String rangeTimeEnd, int productAndMarketTyep, int quantityType) {
        List<MonetaryDisTributionVO> monetaryDisTributionVOList = new ArrayList<>();
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            //查询符合条件的背包物品出入信息
            List<Map> backPackList = gameMonetaryDisTributionMapper.selectAllBackPackByTime(rangeTimeBegin, rangeTimeEnd, productAndMarketTyep, quantityType);
            //背包物品出入信息 以 way 分组
            Map<String, List<Map>> backPackListMap_way = backPackList.stream().collect(Collectors.groupingBy(map -> map.get("way").toString()));
            for (Integer integer : FairyJadeBuyTypeMap.keySet()) {
                //获取当前产销点
                List<Map> oneBackPackListMap_way = backPackListMap_way.get(integer.toString());
                if(null == oneBackPackListMap_way){continue;}
                MonetaryDisTributionVO monetaryDisTributionVO = new MonetaryDisTributionVO();
                //设置产销点
                monetaryDisTributionVO.setProductAndMarket(FairyJadeBuyTypeMap.get(integer));
                //设置货币总和
                Long sum = oneBackPackListMap_way.stream().mapToLong(map -> Long.parseLong(map.get("num").toString())).sum();
                monetaryDisTributionVO.setQuantityOfMoney(sum);
                //设置次数
                monetaryDisTributionVO.setTimes(oneBackPackListMap_way.size());
                //设置人数
                //背包物品出入信息 以 player_id 分组
                Map<String, List<Map>> backPackListMap_playerId = oneBackPackListMap_way.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
                monetaryDisTributionVO.setNumberOfPeople(backPackListMap_playerId.size());
                monetaryDisTributionVOList.add(monetaryDisTributionVO);
            }

        } catch (Exception e) {
           log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
           DataSourceHelper.useDefaultDatabase();
        }
        //设置百分比
        Long allSum = monetaryDisTributionVOList.stream().mapToLong(monetaryDisTribution -> monetaryDisTribution.getQuantityOfMoney()).sum();
        for (MonetaryDisTributionVO monetaryDisTributionVO : monetaryDisTributionVOList) {
            monetaryDisTributionVO.setProportion(BigDecimal.valueOf(monetaryDisTributionVO.getQuantityOfMoney()).divide(BigDecimal.valueOf(allSum),4,BigDecimal.ROUND_HALF_UP));;
        }
        return monetaryDisTributionVOList;
    }
}