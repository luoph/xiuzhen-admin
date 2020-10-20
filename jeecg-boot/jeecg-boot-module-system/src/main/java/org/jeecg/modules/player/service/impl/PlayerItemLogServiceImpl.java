package org.jeecg.modules.player.service.impl;

import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfItem;
import cn.youai.xiuzhen.entity.pojo.ConfRechargeGoods;
import cn.youai.xiuzhen.entity.pojo.ItemReduce;
import cn.youai.xiuzhen.entity.pojo.OperationType;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.simple.Equal;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerItemLog;
import org.jeecg.modules.player.mapper.PlayerItemLogMapper;
import org.jeecg.modules.player.service.IPlayerItemLogService;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
@Service
public class PlayerItemLogServiceImpl extends ServiceImpl<PlayerItemLogMapper, PlayerItemLog> implements IPlayerItemLogService {

	@Resource
	private PlayerItemLogMapper playerItemLogMapper;

	@Autowired
	private ConfigDataService configDataService;

	@Autowired
	private IPlayerService playerService;

	@Override
	public PlayerItemLog writePlayerItemLog(long serverId, BackpackLog backpacklog) {
		return new PlayerItemLog().setServerId(serverId).setPlayerId(backpacklog.getPlayerId()).setType(backpacklog.getType())
				.setWay(backpacklog.getWay()).setSyncTime(backpacklog.getCreateDate()).setItemId(backpacklog.getItemId())
				.setBeforeNum(backpacklog.getBeforeNum()).setAfterNum(backpacklog.getAfterNum())
				.setNum(backpacklog.getNum()).setCreateTime(backpacklog.getCreateTime()).setUpdateTime(DateUtils.now());
	}

	@Override
	public void saveBatchLog(List<PlayerItemLog> logs) {
		DataSourceHelper.useDefaultDatabase();
		saveBatch(logs, 500);
	}

	@Override
	public List<PlayerItemLog> queryCurrencyPayIncomeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId) {
		// 汇总对象
		PlayerItemLog playerItemLog = new PlayerItemLog();
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
		List<PlayerItemLog> incomeList = playerItemLogMapper.queryCurrencyPayIncomeList(rangeDateBeginTime, rangeDateEndTime, serverId, increase, itemId);
		for (PlayerItemLog incomeItemLog : incomeList) {
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
	public List<PlayerItemLog> queryWayDistributeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId, int type) {
		Date rangeDateBeginTime = null;
		Date rangeDateEndTime = null;

		if (days == 0) {
			rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
			rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
		} else {
			rangeDateEndTime = DateUtils.dateOnly(new Date());
			rangeDateBeginTime = DateUtils.dateOnly(DateUtils.addDays(rangeDateEndTime, days * (-1)));
		}

		List<PlayerItemLog> list = playerItemLogMapper.queryWayDistributeList(rangeDateBeginTime, rangeDateEndTime, serverId, itemId, type);
		for (PlayerItemLog playerItemLog : list) {
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
	public List<PlayerItemLog> queryItemBillList(String rangeDateBegin, String rangeDateEnd, int way, Integer serverId, int itemId, int type, Long playerId) {
		List<PlayerItemLog> list = new ArrayList<>();
		try {
			Date rangeDateBeginTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateBegin));
			Date rangeDateEndTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateEnd));

			list = playerItemLogMapper.queryItemBillList(rangeDateBeginTime, rangeDateEndTime, way, playerId, itemId, type);
			DataSourceHelper.useServerDatabase(serverId);
			String nickName = playerService.getNameById(playerId);
			for (PlayerItemLog playerItemLog : list) {
				// 玩家名
				playerItemLog.setPlayerName(nickName);
				// 通过道具获取物品名称
				ConfItem confItem = itemTree(itemId);
				if (confItem == null) {
					playerItemLog.setItemName("该物品不存在");
				} else {
					playerItemLog.setItemName(confItem.getName());
				}
				// 设置物品名称
				ItemReduce itemReduce = ItemReduce.valueOf(way);
				String itemReduceName = itemReduce.getName();
				// 产销点
				playerItemLog.setWayName(itemReduceName);
				String typeName = OperationType.getName(type);
				// 产销类型
				playerItemLog.setTypeName(typeName);
			}
		} catch (Exception e) {
			log.error("切换数据源异常, serverId :" + serverId, e);
		} finally {
			DataSourceHelper.useDefaultDatabase();
		}

		return list;
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
