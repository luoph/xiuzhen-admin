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
		Date rangeDateBeginTime = null;
		Date rangeDateEndTime = null;
		if (days == 0){
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
			if (consumeItemNum == null){
				consumeItemNum = BigDecimal.ZERO;
			}

			incomeItemLog.setConsumeItemNum(consumeItemNum);
			// 消耗比
			incomeItemLog.setConsumeRate(BigDecimalUtil.divideFour(addItemNum.doubleValue(), consumeItemNum.doubleValue(), true));
			// 滞留
			incomeItemLog.setRetention(BigDecimalUtil.subtract(addItemNum.doubleValue(), consumeItemNum.doubleValue()));
		}

		return incomeList;
	}
}
