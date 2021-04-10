package org.jeecg.modules.player.service.impl;

import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfItem;
import cn.youai.xiuzhen.entity.pojo.OperationType;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.constant.ItemReduce;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.GamePlayerItemLog;
import org.jeecg.modules.player.mapper.GamePlayerItemLogMapper;
import org.jeecg.modules.player.service.IGamePlayerItemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.equal;

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

    @Autowired
    private ConfigDataService configDataService;

    @Override
    public GamePlayerItemLog writePlayerItemLog(Integer serverId, BackpackLog backpacklog) {
        return new GamePlayerItemLog().setServerId(serverId).setPlayerId(backpacklog.getPlayerId()).setType(backpacklog.getType())
                .setWay(backpacklog.getWay()).setSyncTime(backpacklog.getCreateDate()).setItemId(backpacklog.getItemId())
                .setBeforeNum(backpacklog.getBeforeNum()).setAfterNum(backpacklog.getAfterNum())
                .setNum(backpacklog.getNum()).setCreateTime(backpacklog.getCreateTime()).setUpdateTime(DateUtils.now());
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

        Date rangeDateBeginTime = null;
        Date rangeDateEndTime = null;
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
        Date rangeDateBeginTime = null;
        Date rangeDateEndTime = null;

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
            // 玩家名
            playerItemLog.setPlayerName(playerItemLog.getRegisterInfo().getName());
            // 通过道具获取物品名称
            ConfItem confItem = itemTree(playerItemLog.getItemId());
            if (confItem == null) {
                playerItemLog.setItemName("该物品不存在");
            } else {
                playerItemLog.setItemName(confItem.getName());
            }
            // 设置物品名称
            ItemReduce itemReduce = ItemReduce.valueOf(playerItemLog.getWay());
            String itemReduceName = itemReduce.getName();
            // 产销点
            playerItemLog.setWayName(itemReduceName);
            String typeName = OperationType.getName(playerItemLog.getType());
            // 产销类型
            playerItemLog.setTypeName(typeName);
        }

        return list;
    }

    @Override
    public List<ConfItem> getConfItemList(Integer itemId, String itemName) {
        QueryOptions queryOptions = QueryFactory.queryOptions(ConfItem.ITEM_ID);
        if (itemId != null && itemName != null) {
            Equal<ConfItem, Integer> equalItemId = QueryFactory.equal(ConfItem.ITEM_ID, itemId);
            Equal<ConfItem, String> equalName = equal(ConfItem.NAME, itemName);
            And<ConfItem> and = QueryFactory.and(equalItemId, equalName);
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, and, queryOptions);
        } else if (itemId != null) {
            Equal<ConfItem, Integer> equal = QueryFactory.equal(ConfItem.ITEM_ID, itemId);
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, equal, queryOptions);
        } else if (itemName != null) {
            Equal<ConfItem, String> equal = equal(ConfItem.NAME, itemName);
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, equal, queryOptions);
        } else {
            return configDataService.selectList(ConfigDataEnum.ITEM, ConfItem.class, queryOptions);
        }
    }

    /**
     * 通物品id获取充值商品(策划表中的数据)
     *
     * @param itemId
     * @return
     */
    private ConfItem itemTree(Integer itemId) {
        Equal<ConfItem, Integer> equal = equal(ConfItem.ITEM_ID, itemId);
        return configDataService.selectOne(ConfigDataEnum.ITEM, ConfItem.class, equal);
    }
}
