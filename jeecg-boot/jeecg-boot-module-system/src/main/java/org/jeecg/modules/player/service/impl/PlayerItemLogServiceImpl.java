package org.jeecg.modules.player.service.impl;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.PlayerItemLog;
import org.jeecg.modules.player.mapper.PlayerItemLogMapper;
import org.jeecg.modules.player.service.IPlayerItemLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public List<PlayerItemLog> queryCurrencyPayIncomeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel, int itemId) {
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
			rangeDateEndTime = new Date();
			rangeDateBeginTime = DateUtils.addDays(rangeDateEndTime, days * (-1));
		}
		// todo 先写死,后面通过枚举来区分拿值
		// 消耗 type
		int income = 1;
		// 新增 type
		int pay = 2;
		// 查询道具新增的数量汇总
		List<PlayerItemLog> incomeList = playerItemLogMapper.queryCurrencyPayIncomeList(rangeDateBeginTime, rangeDateEndTime, serverId, channel, income, itemId);
		for (PlayerItemLog incomeItemLog : incomeList) {
			// 遍历新增的list
			BigDecimal addItemNum = incomeItemLog.getAddItemNum();
			Date syncTime = incomeItemLog.getSyncTime();

			// 查单条获取消耗道具数
			BigDecimal consumeItemNum = playerItemLogMapper.getBySyncTime(syncTime, itemId, pay, serverId);
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
			addItemNumSum = addItemNumSum + addItemNum.doubleValue();

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
}
