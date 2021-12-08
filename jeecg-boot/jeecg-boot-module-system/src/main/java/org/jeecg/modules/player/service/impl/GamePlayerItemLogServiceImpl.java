package org.jeecg.modules.player.service.impl;

import cn.youai.server.conf.ConfItem;
import cn.youai.server.constant.ItemReduce;
import cn.youai.server.constant.ItemRuleId;
import cn.youai.xiuzhen.entity.pojo.OperationType;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.GamePlayerItemLog;
import org.jeecg.modules.player.mapper.GamePlayerItemLogMapper;
import org.jeecg.modules.player.service.IGamePlayerItemLogService;
import org.jeecg.modules.utils.GameConfigUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Service
public class GamePlayerItemLogServiceImpl extends ServiceImpl<GamePlayerItemLogMapper, GamePlayerItemLog> implements IGamePlayerItemLogService {

    @Resource
    private GamePlayerItemLogMapper playerItemLogMapper;

    @Override
    public GamePlayerItemLog writePlayerItemLog(Integer serverId, BackpackLog backpacklog) {
        return new GamePlayerItemLog().setServerId(serverId).setPlayerId(backpacklog.getPlayerId()).setType(backpacklog.getType()).setWay(backpacklog.getWay()).setSyncTime(backpacklog.getCreateDate()).setItemId(backpacklog.getItemId()).setBeforeNum(backpacklog.getBeforeNum()).setAfterNum(backpacklog.getAfterNum()).setNum(backpacklog.getNum()).setCreateTime(backpacklog.getCreateTime()).setUpdateTime(DateUtils.now());
    }

    @Override
    public void saveBatchLog(List<GamePlayerItemLog> logs) {
        DataSourceHelper.useDefaultDatabase();
        saveBatch(logs, 500);
    }

    @Override
    public List<GamePlayerItemLog> queryCurrencyPayIncomeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId) {
        // 汇总对象
        GamePlayerItemLog playerItemLog = new GamePlayerItemLog();
        // 统计临时变量
        double addItemNumSum = 0;
        double consumeItemNumSum = 0;
        BigDecimal consumeRateSumBigDecimal = null;
        BigDecimal retentionSumBigDecimal = null;

        Date rangeDateBeginTime;
        Date rangeDateEndTime;
        if (days == 0) {
            rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
        } else {
            rangeDateEndTime = DateUtils.dateOnly(new Date());
            rangeDateBeginTime = DateUtils.dateOnly(DateUtils.addDays(rangeDateEndTime, days * (-1)));
        }
        // 新增 type
        int increase = OperationType.INCREASE.getType();
        // 消耗 type
        int reduce = OperationType.REDUCE.getType();
        // 查询道具新增的数量汇总
        List<GamePlayerItemLog> incomeList = playerItemLogMapper.queryCurrencyPayIncomeList(rangeDateBeginTime, rangeDateEndTime, serverId, increase, itemId);
        for (GamePlayerItemLog incomeItemLog : incomeList) {
            // 遍历新增的list
            BigDecimal addItemNum = incomeItemLog.getAddItemNum();
            Date syncTime = incomeItemLog.getSyncTime();

            // 查单条获取消耗道具数
            BigDecimal consumeItemNum = playerItemLogMapper.getBySyncTime(syncTime, itemId, reduce, serverId);
            if (consumeItemNum == null) {
                consumeItemNum = BigDecimal.ZERO;
            }

            incomeItemLog.setConsumeItemNum(consumeItemNum);
            // 消耗比
            BigDecimal consumeRate = BigDecimalUtil.divideFour(addItemNum.doubleValue(), consumeItemNum.doubleValue(), true);
            incomeItemLog.setConsumeRate(consumeRate);
            // 滞留
            BigDecimal retention = BigDecimalUtil.subtract(addItemNum.doubleValue(), consumeItemNum.doubleValue());
            incomeItemLog.setRetention(retention);
            // 时间
            // 日期变为字符串显示
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(syncTime);
            incomeItemLog.setDateStr(format);

            // 计算统计对象
            addItemNumSum = addItemNumSum + addItemNum.doubleValue();
            consumeItemNumSum = consumeItemNumSum + consumeItemNum.doubleValue();
        }
        consumeRateSumBigDecimal = BigDecimalUtil.divideFour(addItemNumSum, consumeItemNumSum, true);
        retentionSumBigDecimal = BigDecimalUtil.subtract(addItemNumSum, consumeItemNumSum);

        // 封装统计对象
        playerItemLog.setAddItemNum(new BigDecimal(addItemNumSum));
        playerItemLog.setConsumeItemNum(new BigDecimal(consumeItemNumSum));
        playerItemLog.setConsumeRate(consumeRateSumBigDecimal);
        playerItemLog.setRetention(retentionSumBigDecimal);
        playerItemLog.setDateStr("汇总");

        incomeList.add(0, playerItemLog);

        return incomeList;
    }

    @Override
    public List<GamePlayerItemLog> queryWayDistributeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId, int type) {
        Date rangeDateBeginTime;
        Date rangeDateEndTime;

        if (days == 0) {
            rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
        } else {
            rangeDateEndTime = DateUtils.dateOnly(new Date());
            rangeDateBeginTime = DateUtils.dateOnly(DateUtils.addDays(rangeDateEndTime, days * (-1)));
        }

        List<GamePlayerItemLog> list = playerItemLogMapper.queryWayDistributeList(rangeDateBeginTime, rangeDateEndTime, serverId, itemId, type);
        for (GamePlayerItemLog playerItemLog : list) {
            Integer way = playerItemLog.getWay();

            // 该途径下的道具数
            BigDecimal itemNum = playerItemLog.getItemNum();

            // 全途径下的道具次数总和
            BigDecimal itemNumSum = playerItemLogMapper.queryItemSum(rangeDateBeginTime, rangeDateEndTime, serverId, type);
            // 次数
            BigDecimal itemCount = playerItemLogMapper.queryItemCount(rangeDateBeginTime, rangeDateEndTime, serverId, type, itemId);

            // 占比
            BigDecimal itemNumRate = BigDecimalUtil.divideFour(itemNum.doubleValue(), itemNumSum.doubleValue(), true);

            playerItemLog.setItemCount(itemCount);
            playerItemLog.setItemNumRate(itemNumRate);
            // 设置产销点名字
            ItemReduce itemReduce = ItemReduce.valueOf(way);
            playerItemLog.setWayName(itemReduce.getName());

        }

        return list;
    }

    @Override
    public List<GamePlayerItemLog> queryItemBillList(String rangeDateBegin, String rangeDateEnd, int way, Integer serverId, int itemId, int type, Long playerId) {
        Date rangeDateBeginTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateBegin));
        Date rangeDateEndTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateEnd));
        List<GamePlayerItemLog> list = playerItemLogMapper.queryItemBillList(rangeDateBeginTime, rangeDateEndTime, way, playerId, itemId, type);
        for (GamePlayerItemLog playerItemLog : list) {
            // 通过道具获取物品名称
            ConfItem confItem = GameConfigUtils.getItemById(playerItemLog.getItemId());
            if (confItem == null) {
                playerItemLog.setItemName("该物品不存在");
            } else {
                playerItemLog.setItemName(confItem.getName());
            }
            // 设置物品名称
            ItemReduce itemReduce = ItemReduce.valueOf(playerItemLog.getWay());
            playerItemLog.setWayName(itemReduce.getName());
            String typeName = OperationType.getName(playerItemLog.getType());
            // 产销类型
            playerItemLog.setTypeName(typeName);
        }

        return list;
    }

    @Override
    public IPage<GamePlayerItemLog> queryPlayerItemLogPageList(GamePlayerItemLog playerItemLog, int pageNo, int pageSize) {
        if (playerItemLog == null) {
            return null;
        }

        List<ConfItem> itemList = GameConfigUtils.getConfItemList(playerItemLog.getItemId(), playerItemLog.getItemName());
        if (!CollectionUtils.isEmpty(itemList)) {
            Map<Integer, String> itemMapById = new HashMap<>(itemList.size());
            List<Integer> itemIds = new ArrayList<>(itemList.size());
            itemList.forEach(item -> {
                itemIds.add(item.getItemId());
                itemMapById.put(item.getItemId(), item.getName());
            });

            LambdaQueryWrapper<GamePlayerItemLog> queryWrapper = getQueryWrapper(playerItemLog, itemIds);
            IPage<GamePlayerItemLog> iPage;
            try {
                DataSourceHelper.useServerDatabase(playerItemLog.getServerId());
                Page<GamePlayerItemLog> page = new Page<>(pageNo, pageSize);
                iPage = this.page(page, queryWrapper);
            } finally {
                DataSourceHelper.useDefaultDatabase();
            }

            if (iPage != null && iPage.getSize() > 0) {
                iPage.getRecords().forEach(p -> {
                    p.setItemName(itemMapById.get(p.getItemId()));
                    p.setWayName(ItemRuleId.valueOf(p.getWay()).getName());
                });
            }
            return iPage;
        }
        return null;
    }

    @Override
    public List<GamePlayerItemLog> queryPlayerItemLogList(GamePlayerItemLog playerItemLog) {
        IPage<GamePlayerItemLog> page = queryPlayerItemLogPageList(playerItemLog, 1, Integer.MAX_VALUE);
        if (page != null && page.getSize() > 0) {
            return page.getRecords();
        }
        return null;
    }

    private LambdaQueryWrapper<GamePlayerItemLog> getQueryWrapper(GamePlayerItemLog playerItemLog, List<Integer> itemIds) {
        LambdaQueryWrapper<GamePlayerItemLog> queryWrapper = Wrappers.lambdaQuery();

        if (playerItemLog.getPlayerId() != null && playerItemLog.getPlayerId() > 0) {
            queryWrapper.eq(GamePlayerItemLog::getPlayerId, playerItemLog.getPlayerId());
        }

        if (!CollectionUtils.isEmpty(itemIds)) {
            queryWrapper.in(GamePlayerItemLog::getItemId, itemIds);
        }

        if (!StringUtils.isEmpty(playerItemLog.getWayName())) {
            queryWrapper.in(GamePlayerItemLog::getWay, (Object) playerItemLog.getWayName().split(","));
        }

        if (playerItemLog.getType() != null && playerItemLog.getType() > 0) {
            queryWrapper.eq(GamePlayerItemLog::getType, playerItemLog.getType());
        }

        if (!StringUtils.isAllBlank(playerItemLog.getStartDate(), playerItemLog.getEndDate())) {
            // 如果选择开始时间和结束时间是同一天
            Date startDate = DateUtils.parseDate(playerItemLog.getStartDate());
            Date endDate = DateUtils.parseDate(playerItemLog.getEndDate());
            if (DateUtils.isSameDay(startDate, endDate)) {
                startDate = DateUtils.startTimeOfDate(startDate);
                endDate = DateUtils.endTimeOfDate(endDate);
            }
            queryWrapper.between(GamePlayerItemLog::getCreateTime, startDate, endDate);
        }

        queryWrapper.orderByDesc(GamePlayerItemLog::getCreateTime);

        return queryWrapper;
    }
}
