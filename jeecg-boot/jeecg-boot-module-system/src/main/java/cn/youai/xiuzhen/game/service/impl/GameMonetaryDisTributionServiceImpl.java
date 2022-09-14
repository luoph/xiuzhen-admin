package cn.youai.xiuzhen.game.service.impl;

import cn.youai.basics.model.DateRange;
import cn.youai.server.constant.ItemReduce;
import cn.youai.server.constant.ItemRuleId;
import cn.youai.server.utils.ConvertUtils;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.constant.OperationType;
import cn.youai.xiuzhen.database.DataSourceHelper;
import cn.youai.xiuzhen.game.entity.BackpackLog;
import cn.youai.xiuzhen.game.entity.MonetaryDisTributionVO;
import cn.youai.xiuzhen.game.mapper.GameMonetaryDisTributionMapper;
import cn.youai.xiuzhen.game.service.IGameMonetaryDisTributionService;
import lombok.extern.slf4j.Slf4j;
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
            if (1 == quantityType) {
                for (ItemRuleId value : ItemRuleId.values()) {
                    fairyJadeBuyTypeMap.put(value.getType(), value.getName());
                }
                //消耗
            } else if (2 == quantityType) {
                for (ItemReduce value : ItemReduce.values()) {
                    fairyJadeBuyTypeMap.put(value.getType(), value.getName());
                }
            }


            for (Integer integer : fairyJadeBuyTypeMap.keySet()) {
                //获取当前产销点
                List<Map> oneBackPackListMapWay = backPackListMapWay.get(integer.toString());
                if (null == oneBackPackListMapWay) {
                    continue;
                }
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
            monetaryDisTributionVO.setProportion(BigDecimal.valueOf(monetaryDisTributionVO.getQuantityOfMoney()).divide(BigDecimal.valueOf(allSum), 4, BigDecimal.ROUND_HALF_UP));
            ;
        }
        return monetaryDisTributionVOList;
    }


    @Override
    public Map<Date, List<MonetaryDisTributionVO>> monetaryDistributionList(int channelId, int serverId, DateRange dateRange, int productAndMarketType, int quantityType) {
        if (null == dateRange) {
            return Collections.emptyMap();
        }

        Map<Date, List<MonetaryDisTributionVO>> map = new HashMap<>(DateUtils.daysBetween(dateRange.getStart(), dateRange.getEnd()));
        Map<Integer, String> wayMap = OperationType.getWayMap(quantityType);
        if (null == wayMap) {
            return map;
        }

        // 查询符合条件的背包物品出入信息
        List<BackpackLog> backpackLogList;
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            backpackLogList = gameMonetaryDisTributionMapper.selectAllBackPackByTime2(dateRange.getStart(), dateRange.getEnd(), productAndMarketType, quantityType);
        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
            throw e;
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }

        // 根据日期分组(天)
        Map<Date, List<BackpackLog>> dateBackpackLogMap = backpackLogList.stream().collect(Collectors.groupingBy(BackpackLog::getCreateDate));

        dateBackpackLogMap.forEach((date, list) -> {
            List<MonetaryDisTributionVO> dateResults = new ArrayList<>();
            // 背包物品出入信息 以 way 分组
            Map<Integer, List<BackpackLog>> wayBackpackLogMap = list.stream().collect(Collectors.groupingBy(BackpackLog::getWay));
            wayBackpackLogMap.forEach((k, v) ->
                    dateResults.add(new MonetaryDisTributionVO()
                            .setProductAndMarket(ConvertUtils.safeString(wayMap.get(k), "未知类型"))
                            .setQuantityOfMoney(v.stream().mapToLong(BackpackLog::getNum).sum())
                            .setTimes(v.size())
                            .setNumberOfPeople(v.stream().map(BackpackLog::getPlayerId).collect(Collectors.toSet()).size())));
            if (!dateResults.isEmpty()) {
                // 设置百分比
                BigDecimal sum = BigDecimal.valueOf(dateResults.stream().mapToLong(MonetaryDisTributionVO::getQuantityOfMoney).sum());
                dateResults.forEach(e -> e.setProportion(BigDecimal.valueOf(e.getQuantityOfMoney()).divide(sum, 4, BigDecimal.ROUND_HALF_UP)));
                map.put(date, dateResults);
            }
        });
        return map;
    }
}