package org.jeecg.modules.game.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.constant.ItemRuleId;
import org.jeecg.modules.game.constant.ItemReduce;
import org.jeecg.modules.game.entity.MonetaryDisTributionVO;
import org.jeecg.modules.game.mapper.GameMonetaryDisTributionMapper;
import org.jeecg.modules.game.service.IGameMonetaryDisTributionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 胡立
 * @Description: GameMonetaryDisTributionServiceImpl
 * @date 2020/12/29 10:40
 */

@Slf4j
@Service
public class GameMonetaryDisTributionServiceImpl implements IGameMonetaryDisTributionService {
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
            Map<String, List<Map>> backPackListMapWay = backPackList.stream().collect(Collectors.groupingBy(map -> map.get("way").toString()));

            /**
             * 产销点类型map
             */
            Map<Integer, String> fairyJadeBuyTypeMap = new HashMap<>();
            //存入
            if(1== quantityType){
                for (ItemRuleId value : ItemRuleId.values()) {
                    fairyJadeBuyTypeMap.put(value.getId(), value.getName());
                }
            //消耗
            }else if(2== quantityType){
                for (ItemReduce value : ItemReduce.values()) {
                    fairyJadeBuyTypeMap.put(value.getId(), value.getName());
                }
            }


            for (Integer integer : fairyJadeBuyTypeMap.keySet()) {
                //获取当前产销点
                List<Map> oneBackPackListMapWay = backPackListMapWay.get(integer.toString());
                if(null == oneBackPackListMapWay){continue;}
                MonetaryDisTributionVO monetaryDisTributionVO = new MonetaryDisTributionVO();
                //设置产销点
                monetaryDisTributionVO.setProductAndMarket(fairyJadeBuyTypeMap.get(integer));
                //设置货币总和
                Long sum = oneBackPackListMapWay.stream().mapToLong(map -> Long.parseLong(map.get("num").toString())).sum();
                monetaryDisTributionVO.setQuantityOfMoney(sum);
                //设置次数
                monetaryDisTributionVO.setTimes(oneBackPackListMapWay.size());
                //设置人数
                //背包物品出入信息 以 player_id 分组
                Map<String, List<Map>> backPackListMapPlayerId = oneBackPackListMapWay.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
                monetaryDisTributionVO.setNumberOfPeople(backPackListMapPlayerId.size());
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