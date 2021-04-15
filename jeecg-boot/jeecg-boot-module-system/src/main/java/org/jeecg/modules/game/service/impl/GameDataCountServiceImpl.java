package org.jeecg.modules.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.entity.*;
import org.jeecg.modules.game.mapper.GameCountOngoingMapper;
import org.jeecg.modules.game.mapper.GameDataRemainMapper;
import org.jeecg.modules.game.mapper.GameDayDataCountMapper;
import org.jeecg.modules.game.mapper.GameLtvCountMapper;
import org.jeecg.modules.game.service.*;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.jeecg.modules.game.util.ReflectUtils;
import org.jeecg.modules.player.service.ILogAccountService;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName IGameDataCountServiceImpl
 * @Description 数据统计业务实现
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-21 11:07
 */
@Service
@Slf4j
public class GameDataCountServiceImpl implements IGameDataCountService {
	/**
	 * 统计时长30天
	 */
	private static final int DAYS_BETWEEN = 30;
	/**
	 * 连续统计字段前缀
	 */
	private static final String FIELD = "c";
	/**
	 * 留存间隔查询
	 * 统计间隔+1 = 统计天数
	 */
	private static final int[] REMAIN = new int[]{1, 2, 3, 4, 5, 6, 14, 29, 59, 89, 119, 179, 359};
	/**
	 * ltv统计间隔
	 * 统计间隔 = 统计天数
	 */
	private static final int[] LTV = new int[]{1, 2, 3, 4, 5, 6, 7, 14, 21, 30, 60, 90, 120, 180, 360};
	@Autowired
	private IGameChannelService gameChannelService;
	@Autowired
	private IGameServerService gameServerService;
	@Autowired
	private IGameChannelServerService gameChannelServerService;
	@Autowired
	private IPayOrderService payOrderService;
	@Autowired
	private ILogAccountService logAccountService;
	@Autowired
	private IGameDataRemainService gameDataRemainService;
	@Autowired
	private IGameLtvCountService gameLtvCountService;
	@Resource
	private GameDayDataCountMapper gameDayDataCountMapper;
	@Resource
	private GameLtvCountMapper gameLtvCountMapper;
	@Resource
	private GameDataRemainMapper gameDataRemainMapper;
	@Autowired
	private IGameCountOngoingService gameCountOngoingService;
	@Resource
	private GameCountOngoingMapper gameCountOngoingMapper;
	@Autowired
	private IGameDayDataCountService gameDayDataCountService;
	@Value("${app.log.db.table}")
	private String logTable;

	@Override
	public List<GameStatDaily> queryDateRangeDataCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
		return queryDateRangeDataCount(null, gameChannel, gameServer, rangeDateBegin, rangeDateEnd, isOpenDateCount);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameStatDaily> queryDateRangeDataCount(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
		Map<String, GameStatDaily> map;
		if (context != null) {
			map = (Map<String, GameStatDaily>) context.get(KEY_GAME_STAT_DAILY_COUNT_MAP);
		} else {
			map = dailyCountMap(true);
		}
		Date dateBegin = DateUtils.parseDate(rangeDateBegin);
		Date dateEnd = DateUtils.parseDate(rangeDateEnd);
		// 数组第一个元素为开始统计的第一个日期
		Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
		int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
		List<GameStatDaily> list = new ArrayList<>(dateRangeBetween);
		for (int i = 0; i <= dateRangeBetween; i++) {
			String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
			String dailyCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
			GameStatDaily gameDayDataCount = map.get(dailyCountKey);
			if (gameDayDataCount != null) {
				continue;
			}
			GameStatDaily gameDataCount = gameDataCount(gameChannel, gameServer, dateOnly);
			list.add(gameDataCount);
			map.put(dailyCountKey, gameDataCount);
		}
		return list;
	}

	@Override
	public Map<String, GameStatDaily> dailyCountMap(boolean isReturnIndex) {
		List<GameStatDaily> dataCounts;
		if (isReturnIndex) {
			LambdaQueryWrapper<GameStatDaily> queryWrapper = Wrappers.lambdaQuery();
			queryWrapper.select(GameStatDaily::getChannel, GameStatDaily::getServerId, GameStatDaily::getCountDate);
			dataCounts = gameDayDataCountService.list(queryWrapper);
		} else {
			dataCounts = gameDayDataCountService.list();
		}
		Map<String, GameStatDaily> map = new HashMap<>(dataCounts.size());
		for (GameStatDaily dataCount : dataCounts) {
			String formatDate = DateUtils.formatDate(dataCount.getCountDate(), DatePattern.NORM_DATE_PATTERN);
			String countKey = dailyCountKey(dataCount.getChannel(), dataCount.getServerId(), formatDate);
			map.put(countKey, dataCount);
		}
		return map;
	}


	private String dailyCountKey(String channel, int serverId, String countDate) {
		return channel + "_" + serverId + "_" + countDate;
	}


	@Override
	public GameStatDaily gameDataCount(GameChannel gameChannel, GameServer gameServer, String date) {

		//当天付费总金额
		double sumPayAmount = payOrderService.sumPayAmount(gameChannel.getSimpleName(), gameServer.getId(), date);
		// 支付玩家数
		int countPay = payOrderService.countPayPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);
		// 当天登录角色数
		int loginNum = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date, 2);
		// 当天注册角色数
		int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date, 1);
		// 注册付费总金额
		double registerPayAmount = logAccountService.registerPayAmount(gameChannel.getSimpleName(), gameServer.getId(), date);
		// 注册付费玩家
		int registerPayPlayer = logAccountService.registerPayPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);
		// 注册二次付费玩家
		int doublePayPlayer = logAccountService.doublePayRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), date);


		return new GameStatDaily().setPayAmount(BigDecimalUtil.valueOf(sumPayAmount)).setLoginNum(loginNum)
				.setPayNum(countPay).setArpu(BigDecimalUtil.divideZero(sumPayAmount, loginNum, false))
				.setArppu(BigDecimalUtil.divideZero(sumPayAmount, countPay, false))
				.setPayRate(BigDecimalUtil.divideZero(countPay, loginNum, true))
				.setAddNum(registerPlayer).setAddPayNum(registerPayPlayer)
				.setAddPayAmount(BigDecimalUtil.valueOf(registerPayAmount))
				.setAddPayRate(BigDecimalUtil.divideZero(registerPayPlayer, registerPlayer, true))
				.setDoublePay(doublePayPlayer)
				.setDoublePayRate(BigDecimalUtil.divideZero(doublePayPlayer, registerPayPlayer, true))
				.setAddArpu(BigDecimalUtil.divideZero(registerPayAmount, registerPlayer, false))
				.setAddArppu(BigDecimalUtil.divideZero(registerPayAmount, registerPayPlayer, false))
				.setChannel(gameChannel.getSimpleName()).setServerId(gameServer.getId())
				.setCountDate(DateUtils.parseDate(date)).setCreateTime(DateUtils.now());
	}

	@Override
	public void doJobDataCount() {
		List<GameChannelServer> list = gameChannelServerService.list();
		list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
		Date date = DateUtils.addDays(DateUtils.todayDate(), -1);
		String formatDate = DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN);
		Map<String, Object> context = new HashMap<>();
		// 统计数据
		context.put(KEY_GAME_STAT_DAILY_COUNT_MAP, dailyCountMap(true));
		// 留存数据
		context.put(KEY_GAME_STAT_REMAIN_COUNT_MAP, remainCountMap(true));
		// ltv数据
		context.put(KEY_GAME_STAT_LTV_COUNT_MAP, ltvCountMap(true));

		for (GameChannelServer gameChannelServer : list) {
			GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
			GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
			if (DateUtils.daysBetween(gameServer.getOpenTime(), date) < 0) {
				continue;
			}

			String f = DateUtils.formatDate(gameServer.getOpenTime(), DatePattern.NORM_DATETIME_PATTERN);
			List<GameStatDaily> gameDayDataCounts = queryDateRangeDataCount(context, gameChannel, gameServer, f, formatDate, false);
			if (CollUtil.isNotEmpty(gameDayDataCounts)) {
				gameDayDataCountMapper.updateOrInsert(gameDayDataCounts);
			}

			List<GameStatRemain> gameDataRemains = queryDataRemainCount(context, gameChannel, gameServer, f, formatDate, false);
			if (CollUtil.isNotEmpty(gameDataRemains)) {
				gameDataRemainMapper.updateOrInsert(gameDataRemains);
			}

			List<GameStatLtv> gameLtvCounts = queryDataLtvCount(context, gameChannel, gameServer, f, formatDate);
			if (CollUtil.isNotEmpty(gameLtvCounts)) {
				gameLtvCountMapper.updateOrInsert(gameLtvCounts);
			}
		}
		List<GameStatOngoing> gameCountOngoings = countOngoings();
		gameCountOngoingMapper.insertOrUpdateList(gameCountOngoings);
	}

	@Override
	public void doJobDataCountUpdate() {
		long startTime = System.currentTimeMillis();
		List<GameChannelServer> list = gameChannelServerService.list();
		list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
		Date date = DateUtils.addDays(DateUtils.todayDate(), -1);
		String formatDate = DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN);
		Map<String, Object> context = new HashMap<>();
		// 留存数据
		context.put(KEY_GAME_STAT_REMAIN_COUNT_MAP, remainCountMap(false));
		// ltv数据
		context.put(KEY_GAME_STAT_LTV_COUNT_MAP, ltvCountMap(false));
		context.put("num", 0);
		context.put("sum", 0);
		for (GameChannelServer gameChannelServer : list) {
			GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
			GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
			if (DateUtils.daysBetween(gameServer.getOpenTime(), date) < 0) {
				continue;
			}
			try {
				long updateRemainTaskStartTime = System.currentTimeMillis();
				// 留存更新
				updateRemainTask(context, gameChannel, gameServer, formatDate);
				log.info("simpleName={},serverId={},执行【doJobDataCountUpdate-updateRemainTask】耗时：{}ms",
						gameChannel.getSimpleName(), gameChannelServer.getServerId(),
						(System.currentTimeMillis() - updateRemainTaskStartTime));
			} catch (Exception e) {
				log.error("updateRemainTask error!" + gameChannelServer.getChannelId() + "_" + gameChannelServer.getServerId());
			}

			try {
				long updateLtvTaskStartTime = System.currentTimeMillis();
				// ltv更新
				updateLtvTask(context, gameChannel, gameServer, formatDate);
				log.info("simpleName={},serverId={},执行【doJobDataCountUpdate-updateRemainTask】耗时：{}ms",
						gameChannel.getSimpleName(), gameChannelServer.getServerId(),
						(System.currentTimeMillis() - updateLtvTaskStartTime));
			} catch (Exception e) {
				log.error("updateLtvTask error!" + gameChannelServer.getChannelId() + "_" + gameChannelServer.getServerId());
			}
		}
		log.info("【doJobDataCountUpdate】执行计算次数：" + context.get("num"));
		log.info("【doJobDataCountUpdate】处理数据量：" + context.get("sum"));
		log.info("【doJobDataCountUpdate】接口耗时：" + (System.currentTimeMillis() - startTime) + "ms");
	}

	@Override
	public List<GameStatRemain> queryDataRemainCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
		return queryDataRemainCount(null, gameChannel, gameServer, rangeDateBegin, rangeDateEnd, isOpenDateCount);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameStatRemain> queryDataRemainCount(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount) {
		Map<String, GameStatRemain> map;
		if (context != null) {
			map = (Map<String, GameStatRemain>) context.get(KEY_GAME_STAT_REMAIN_COUNT_MAP);
		} else {
			map = remainCountMap(true);
		}
		Date dateBegin = DateUtils.parseDate(rangeDateBegin);
		Date dateEnd = DateUtils.parseDate(rangeDateEnd);
		// 数组第一个元素为开始统计的第一个日期
		Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
		int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
		List<GameStatRemain> list = new ArrayList<>(dateRangeBetween);
		for (int i = 0; i <= dateRangeBetween; i++) {
			String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
			String remainCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
			GameStatRemain dataRemain = map.get(remainCountKey);
			if (dataRemain != null) {
				continue;
			}
			GameStatRemain gameDataRemain = gameDataRemainService.getCountRemain(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
			list.add(gameDataRemain);
			map.put(remainCountKey, gameDataRemain);
		}
		return list;
	}

	@Override
	public Map<String, GameStatRemain> remainCountMap(boolean isReturnIndex) {
		List<GameStatRemain> dataCounts;
		if (isReturnIndex) {
			LambdaQueryWrapper<GameStatRemain> queryWrapper = Wrappers.lambdaQuery();
			queryWrapper.select(GameStatRemain::getChannel, GameStatRemain::getServerId, GameStatRemain::getCountDate);
			dataCounts = gameDataRemainService.list(queryWrapper);
		} else {
			dataCounts = gameDataRemainService.list();
		}
		Map<String, GameStatRemain> map = new HashMap<>(dataCounts.size());
		for (GameStatRemain remainCount : dataCounts) {
			String formatDate = DateUtils.formatDate(remainCount.getCountDate(), DatePattern.NORM_DATE_PATTERN);
			String countKey = dailyCountKey(remainCount.getChannel(), remainCount.getServerId(), formatDate);
			map.put(countKey, remainCount);
		}
		return map;
	}


	@Override
	public List<GameStatLtv> queryDataLtvCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
		return queryDataLtvCount(null, gameChannel, gameServer, rangeDateBegin, rangeDateEnd);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameStatLtv> queryDataLtvCount(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
		Map<String, GameStatLtv> map;
		if (context != null) {
			map = (Map<String, GameStatLtv>) context.get(KEY_GAME_STAT_LTV_COUNT_MAP);
		} else {
			map = ltvCountMap(true);
		}
		Date dateBegin = DateUtils.parseDate(rangeDateBegin);
		Date dateEnd = DateUtils.parseDate(rangeDateEnd);
		// 数组第一个元素为开始统计的第一个日期
		Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
		int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
		List<GameStatLtv> list = new ArrayList<>(dateRangeBetween);
		for (int i = 0; i <= dateRangeBetween; i++) {
			String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
			String ltvCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), dateOnly);
			GameStatLtv ltvCount = map.get(ltvCountKey);
			if (ltvCount != null) {
				continue;
			}
			GameStatLtv gameLtvCount = gameLtvCountService.getGameLtvCount(gameChannel.getSimpleName(), gameServer.getId(), dateOnly, logTable);
			list.add(gameLtvCount);
			map.put(ltvCountKey, gameLtvCount);
		}
		return list;
	}

	@Override
	public Map<String, GameStatLtv> ltvCountMap(boolean isReturnIndex) {
		List<GameStatLtv> dataCounts;
		if (isReturnIndex) {
			LambdaQueryWrapper<GameStatLtv> queryWrapper = Wrappers.lambdaQuery();
			queryWrapper.select(GameStatLtv::getChannel, GameStatLtv::getServerId, GameStatLtv::getCountDate);
			dataCounts = gameLtvCountService.list(queryWrapper);
		} else {
			dataCounts = gameLtvCountService.list();
		}
		Map<String, GameStatLtv> map = new HashMap<>(dataCounts.size());
		for (GameStatLtv ltvCount : dataCounts) {
			String formatDate = DateUtils.formatDate(ltvCount.getCountDate(), DatePattern.NORM_DATE_PATTERN);
			String countKey = dailyCountKey(ltvCount.getChannel(), ltvCount.getServerId(), formatDate);
			map.put(countKey, ltvCount);
		}
		return map;
	}


	private int betweenNatural(GameServer gameServer, String countDate) {
		Date openTime = gameServer.getOpenTime();
		Date date = DateUtils.parseDate(countDate);
		if (openTime.after(date)) {
			return 0;
		}
		int betweenNatural = DateUtils.daysBetweenNatural(openTime, date);
		return Math.max(betweenNatural, 0);
	}


	@Override
	@SuppressWarnings("unchecked")
	public void updateRemainTask(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String countDate) {
		Map<String, GameStatRemain> map;
		if (context != null) {
			map = (Map<String, GameStatRemain>) context.get(KEY_GAME_STAT_REMAIN_COUNT_MAP);
		} else {
			map = remainCountMap(false);
		}
		int betweenNatural = betweenNatural(gameServer, countDate);
		List<GameStatRemain> list = new ArrayList<>();
		for (int i = 0; i <= betweenNatural; i++) {
			Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
			int leftDays = DateUtils.daysBetweenNatural(nextDate, DateUtils.parseDate(countDate));
			String formatDate = DateUtils.formatDate(nextDate, DatePattern.NORM_DATE_PATTERN);
			String remainCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), formatDate);
			GameStatRemain gameRemainCount = map.get(remainCountKey);
			if (gameRemainCount == null || gameRemainCount.getD360Remain() != null) {
				continue;
			}

			Date startRegisterDate = DateUtils.startTimeOfDate(nextDate);
			Date endRegisterDate = DateUtils.endTimeOfDate(nextDate);
			for (int j = 1; j <= leftDays; j++) {
				if (isNeedStatRemain(j, leftDays)) {
					log.info("updateRemainTask param is,simpleName={}, serverId={}, nextDate={}, leftDays={}", gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), j);
					int remain = gameDataRemainMapper.selectRemain(gameChannel.getSimpleName(), gameServer.getId(),
							DateUtils.formatDateTimeStr(startRegisterDate),
							DateUtils.formatDateTimeStr(endRegisterDate),
							DateUtils.formatDateTimeStr(DateUtils.addDays(startRegisterDate, j)),
							DateUtils.formatDateTimeStr(DateUtils.addDays(endRegisterDate, j)),
							logTable);
					updateRemainCountField(gameRemainCount, j, remain);
					Integer num = (Integer) context.get("num");
					num++;
					context.put("num", num);
				}
			}
//			map.put(remainCountKey, gameRemainCount);
			list.add(gameRemainCount);
			Integer sum = (Integer) context.get("sum");
			sum++;
			context.put("sum", sum);
		}
//		List<GameStatRemain> remains = Lists.newArrayList(map.values());
		long l = System.currentTimeMillis();
		gameDataRemainService.updateBatchById(list);
		log.info("---------批量更新数量：" + list.size() + ",----------------------gameLtvCountService.updateBatchById(ltvCounts) 耗时：" + (System.currentTimeMillis() - l) + "ms");
	}

	/**
	 * 是否需要统计计算
	 * 跨越天数统计最大值
	 * 1,2,3,4,5,6,---(跨越天数不统计)---15,---(跨越天数不统计)---30,---(跨越天数不统计)---60,---(跨越天数不统计)---90
	 *
	 * @param num        统计间隔
	 * @param maxDateNum 最大时间间隔
	 */
	private boolean isNeedStatRemain(int num, int maxDateNum) {
		if (num > 0 && num <= REMAIN[5]) {
			return true;
		} else if (num > REMAIN[5] && num <= REMAIN[6]) {
			if (maxDateNum > REMAIN[6]) {
				return num == REMAIN[6];
			} else {
				return num == maxDateNum;
			}
		} else if (num > REMAIN[6] && num <= REMAIN[7]) {
			if (maxDateNum > REMAIN[7]) {
				return num == REMAIN[7];
			} else {
				return num == maxDateNum;
			}
		} else if (num > REMAIN[7] && num <= REMAIN[8]) {
			if (maxDateNum > REMAIN[8]) {
				return num == REMAIN[8];
			} else {
				return num == maxDateNum;
			}
		} else if (num > REMAIN[8] && num <= REMAIN[9]) {
			if (maxDateNum > REMAIN[9]) {
				return num == REMAIN[9];
			} else {
				return num == maxDateNum;
			}
		} else if (num > REMAIN[9] && num <= REMAIN[10]) {
			if (maxDateNum > REMAIN[10]) {
				return num == REMAIN[10];
			} else {
				return num == maxDateNum;
			}
		} else if (num > REMAIN[10] && num <= REMAIN[11]) {
			if (maxDateNum > REMAIN[11]) {
				return num == REMAIN[11];
			} else {
				return num == maxDateNum;
			}
		} else if (num > REMAIN[11] && num <= REMAIN[12]) {
			if (maxDateNum > REMAIN[12]) {
				return num == REMAIN[12];
			} else {
				return num == maxDateNum;
			}
		}
		return false;
	}

	private void updateRemainCountField(GameStatRemain gameDataRemain, int j, int remain) {
		if (gameDataRemain != null) {
			if (j > 0 && j <= REMAIN[0]) {
				gameDataRemain.setD2Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[0] && j <= REMAIN[1]) {
				gameDataRemain.setD3Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[1] && j <= REMAIN[2]) {
				gameDataRemain.setD4Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[2] && j <= REMAIN[3]) {
				gameDataRemain.setD5Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[3] && j <= REMAIN[4]) {
				gameDataRemain.setD6Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[4] && j <= REMAIN[5]) {
				gameDataRemain.setD7Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[5] && j <= REMAIN[6]) {
				gameDataRemain.setD15Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[6] && j <= REMAIN[7]) {
				gameDataRemain.setD30Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[7] && j <= REMAIN[8]) {
				gameDataRemain.setD60Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[8] && j <= REMAIN[9]) {
				gameDataRemain.setD90Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[9] && j <= REMAIN[10]) {
				gameDataRemain.setD120Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[10] && j <= REMAIN[11]) {
				gameDataRemain.setD180Remain(BigDecimal.valueOf(remain));
			} else if (j > REMAIN[11] && j <= REMAIN[12]) {
				gameDataRemain.setD360Remain(BigDecimal.valueOf(remain));
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void updateLtvTask(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String countDate) {
		Map<String, GameStatLtv> map;
		if (context != null) {
			map = (Map<String, GameStatLtv>) context.get(KEY_GAME_STAT_LTV_COUNT_MAP);
		} else {
			map = ltvCountMap(false);
		}

		int betweenNatural = betweenNatural(gameServer, countDate);
		List<GameStatLtv> list = new ArrayList<>();
		for (int i = 0; i <= betweenNatural; i++) {
			Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
			int leftDays = DateUtils.daysBetweenNatural(nextDate, DateUtils.parseDate(countDate));
			String formatDate = DateUtils.formatDate(nextDate, DatePattern.NORM_DATE_PATTERN);
			String ltvCountKey = dailyCountKey(gameChannel.getSimpleName(), gameServer.getId(), formatDate);
			GameStatLtv gameLtvCount = map.get(ltvCountKey);
			if (gameLtvCount == null || gameLtvCount.getD360Amount() != null) {
				continue;
			}

			Date startRegisterDate = DateUtils.startTimeOfDate(nextDate);
			Date endRegisterDate = DateUtils.endTimeOfDate(nextDate);
			for (int j = 1; j <= leftDays; j++) {
				Integer num = (Integer) context.get("num");
				if (isNeedStatLtvRemain(j, leftDays)) {
					log.info("updateLtvTask param is,simpleName={}, serverId={}, nextDate={}, leftDays={}", gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), j);
					double remain = gameLtvCountMapper.selectLtv(gameChannel.getSimpleName(), gameServer.getId(),
							DateUtils.formatDateTimeStr(startRegisterDate),
							DateUtils.formatDateTimeStr(endRegisterDate),
							DateUtils.formatDateTimeStr(DateUtils.addDays(startRegisterDate, j)),
							logTable);
					updateLtvCountField(gameLtvCount, j, remain);
				}
				num++;
				context.put("num", num);
			}
			list.add(gameLtvCount);
			Integer sum = (Integer) context.get("sum");
			sum++;
			context.put("sum", sum);
//			map.put(ltvCountKey, gameLtvCount);
		}
		// 批量更新
//		List<GameStatLtv> ltvCounts = Lists.newArrayList(map.values());
		long l = System.currentTimeMillis();
		gameLtvCountService.updateBatchById(list);
		log.info("---------批量更新数量：" + list.size() + ",----------------------gameLtvCountService.updateBatchById(ltvCounts) 耗时：" + (System.currentTimeMillis() - l) + "ms");
	}

	/**
	 * 是否需要统计计算
	 * 跨越天数统计最大值
	 * 1,2,3,4,5,6,7--(跨越天数不统计)--14,--(跨越天数不统计)--21,--(跨越天数不统计)---30,--(跨越天数不统计)---90...
	 *
	 * @param num        统计间隔
	 * @param maxDateNum 最大时间间隔
	 */
	private boolean isNeedStatLtvRemain(int num, int maxDateNum) {
		if (num > 0 && num <= LTV[6]) {
			return true;
		} else if (num > LTV[6] && num <= LTV[7]) {
			if (maxDateNum > LTV[7]) {
				return num == LTV[7];
			} else {
				return num == maxDateNum;
			}
		} else if (num > LTV[7] && num <= LTV[8]) {
			if (maxDateNum > LTV[8]) {
				return num == LTV[8];
			} else {
				return num == maxDateNum;
			}
		} else if (num > LTV[8] && num <= LTV[9]) {
			if (maxDateNum > LTV[9]) {
				return num == LTV[9];
			} else {
				return num == maxDateNum;
			}
		} else if (num > LTV[9] && num <= LTV[10]) {
			if (maxDateNum > LTV[10]) {
				return num == LTV[10];
			} else {
				return num == maxDateNum;
			}
		} else if (num > LTV[10] && num <= LTV[11]) {
			if (maxDateNum > LTV[11]) {
				return num == LTV[11];
			} else {
				return num == maxDateNum;
			}
		} else if (num > LTV[11] && num <= LTV[12]) {
			if (maxDateNum > LTV[12]) {
				return num == LTV[12];
			} else {
				return num == maxDateNum;
			}
		} else if (num > LTV[12] && num <= LTV[13]) {
			if (maxDateNum > LTV[13]) {
				return num == LTV[13];
			} else {
				return num == maxDateNum;
			}
		} else if (num > LTV[13] && num <= LTV[14]) {
			if (maxDateNum > LTV[14]) {
				return num == LTV[14];
			} else {
				return num == maxDateNum;
			}
		}
		return false;
	}

	private void updateLtvCountField(GameStatLtv gameLtvCount, int j, double remain) {
		if (gameLtvCount != null) {
			if (j > 0 && j <= LTV[0]) {
				gameLtvCount.setD1Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[0] && j <= LTV[1]) {
				gameLtvCount.setD2Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[1] && j <= LTV[2]) {
				gameLtvCount.setD3Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[2] && j <= LTV[3]) {
				gameLtvCount.setD4Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[3] && j <= LTV[4]) {
				gameLtvCount.setD5Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[4] && j <= LTV[5]) {
				gameLtvCount.setD6Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[5] && j <= LTV[6]) {
				gameLtvCount.setD7Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[6] && j <= LTV[7]) {
				gameLtvCount.setD14Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[7] && j <= LTV[8]) {
				gameLtvCount.setD21Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[8] && j <= LTV[9]) {
				gameLtvCount.setD30Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[9] && j <= LTV[10]) {
				gameLtvCount.setD60Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[10] && j <= LTV[11]) {
				gameLtvCount.setD90Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[11] && j <= LTV[12]) {
				gameLtvCount.setD120Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[12] && j <= LTV[13]) {
				gameLtvCount.setD180Amount(BigDecimal.valueOf(remain));
			} else if (j > LTV[13] && j <= LTV[14]) {
				gameLtvCount.setD360Amount(BigDecimal.valueOf(remain));
			}
		}
	}

	@Override
	public List<GameStatOngoing> countOngoings() {
		List<GameChannelServer> list = gameChannelServerService.list();
		list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0
				&& gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
		Date countDate = DateUtils.addDays(DateUtils.todayDate(), -1);
		Map<String, GameStatOngoing> ongoingMap = countOngoingMap();
		for (GameChannelServer gameChannelServer : list) {
			GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
			GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
			int betweenNatural = betweenNatural(gameServer, DateUtils.formatDate(countDate, DatePattern.NORM_DATETIME_PATTERN));
			if (betweenNatural <= 0) {
				continue;
			}
			int[] types = new int[]{1, 2}; // 1-留存 2-ltv
			for (int type : types) {
				countOngoingsByDays(gameChannel, gameServer, countDate, betweenNatural, type, ongoingMap);
			}
		}
		return Lists.newArrayList(ongoingMap.values());
	}

	private Map<String, GameStatOngoing> countOngoingMap() {
		Map<String, GameStatOngoing> map = new HashMap<>();
		List<GameStatOngoing> list = gameCountOngoingService.list();
		for (GameStatOngoing gameCountOngoing : list) {
			String mapKey = mapKey(gameCountOngoing);
			map.put(mapKey, gameCountOngoing);
		}
		return map;
	}

	private String mapKey(GameStatOngoing m) {
		return m.getChannel() + "_" + m.getServerId() + "_" + m.getType() + "_"
				+ DateUtils.formatDate(m.getCountDate(), DatePattern.NORM_DATE_PATTERN);
	}

	private void countOngoingsByDays(GameChannel gameChannel, GameServer gameServer, Date countDate,
									 int betweenNatural, int type, Map<String, GameStatOngoing> ongoingMap) {
		for (int i = 0; i <= betweenNatural; i++) {
			Date nextDate = DateUtils.addDays(gameServer.getOpenTime(), i);
			int leftDays = DateUtils.daysBetweenNatural(nextDate, countDate);
			GameStatOngoing keyObj = new GameStatOngoing().setChannel(gameChannel.getSimpleName())
					.setServerId(gameServer.getId()).setCountDate(nextDate).setType(type);
			String mapKey = mapKey(keyObj);// 留存
			GameStatOngoing gameCountOngoing = ongoingMap.get(mapKey);
			if (gameCountOngoing == null) {
				// 插入新纪录
				int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), DateUtils.formatDate(nextDate, DatePattern.NORM_DATE_PATTERN), 1);
				keyObj.setRegisterNum((long) registerPlayer);
				gameCountOngoing = keyObj;
			}
			if (gameCountOngoing.getC30() == null) {
				leftDays = Math.min(leftDays, DAYS_BETWEEN);
				// 更新统计
				for (int j = 0; j <= leftDays; j++) {
					String countField = FIELD;
					if (j == 0) {
						countField += 1;
					} else {
						countField += j;
					}
					BigDecimal getField = (BigDecimal) ReflectUtils.invokeGetField(gameCountOngoing, countField);
					if (getField == null) {
						double countValue;
						// type 1-留存 2-ltv
						if (type == 1) {
							countValue = gameDataRemainMapper.selectRemainAutoDate(gameChannel.getSimpleName(),
									gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), logTable, j);
						} else {
							countValue = gameLtvCountMapper.selectLtvAutoDate(gameChannel.getSimpleName(),
									gameServer.getId(), DateUtils.formatDateTimeStr(nextDate), j);
						}
						ReflectUtils.invokeSetField(gameCountOngoing, countField, countValue);

						ongoingMap.put(mapKey, gameCountOngoing);
					}
				}
			}
		}
	}

	@Override
	public List<GameStatOngoing> queryCountOnGoing(int type, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd) {
		List<GameStatOngoing> list = new ArrayList<>();
		Date dateBegin = DateUtils.parseDate(rangeDateBegin);
		Date dateEnd = DateUtils.parseDate(rangeDateEnd);
		// 数组第一个元素为开始统计的第一个日期
		Date[] dates = ParamValidUtil.dateBegin(dateBegin, dateEnd);
		int dateRangeBetween = ParamValidUtil.dateRangeBetween(dateBegin, dateEnd);
		for (int i = 0; i <= dateRangeBetween; i++) {
			String dateOnly = DateUtils.formatDate(DateUtils.addDays(dates[0], i), DatePattern.NORM_DATE_PATTERN);
			int registerPlayer = logAccountService.loginRegisterPlayer(gameChannel.getSimpleName(), gameServer.getId(), dateOnly, 1);

			GameStatOngoing gameCountOngoing = new GameStatOngoing().setChannel(gameChannel.getSimpleName())
					.setServerId(gameServer.getId()).setCountDate(DateUtils.parseDate(dateOnly))
					.setRegisterNum((long) registerPlayer).setType(type).setC1(BigDecimal.valueOf(100));
			list.add(gameCountOngoing);
		}
		return list;
	}
}
