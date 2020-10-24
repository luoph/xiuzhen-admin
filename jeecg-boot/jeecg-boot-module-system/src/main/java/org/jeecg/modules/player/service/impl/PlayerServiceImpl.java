package org.jeecg.modules.player.service.impl;

import cn.youai.xiuzhen.entity.pojo.PlayerLogType;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.mapper.PayOrderGiftMapper;
import org.jeecg.modules.game.mapper.PayUserRankMapper;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerBehavior;
import org.jeecg.modules.player.entity.PlayerDTO;
import org.jeecg.modules.player.entity.PlayerRegisterInfo;
import org.jeecg.modules.player.mapper.PlayerMapper;
import org.jeecg.modules.player.mapper.PlayerRegisterInfoMapper;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @ 2019-12-14
 */
@Slf4j
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements IPlayerService {

	@Resource
	private PlayerMapper playerMapper;

	@Resource
	private PayOrderBillMapper payOrderBillMapper;

	@Resource
	private PlayerRegisterInfoMapper playerRegisterInfoMapper;

	@Resource
	private PayUserRankMapper payUserRankMapper;

	@Value("${app.playerLog.db.table}")
	private String logTable;

	@Override
	public List<Player> queryForList(PlayerDTO playerDTO) {
		List<Player> list = new ArrayList<>();
		try {
			DataSourceHelper.useServerDatabase(playerDTO.getServerId());
			// 如果选择开始时间和结束时间是同一天
			String createBegin = playerDTO.getCreateBegin();
			String createEnd = playerDTO.getCreateEnd();
			String loginBegin = playerDTO.getLoginBegin();
			String loginEnd = playerDTO.getLoginEnd();
			// 时间不为空
			// 时间为同一天
			if (createBegin != null && createEnd != null && createBegin.equals(createEnd)) {
				createBegin = createBegin + " 00:00:00";
				createEnd = createEnd + " 23:59:59";
			}
			playerDTO.setCreateDateBegin(DateUtils.parseDate(createBegin));
			playerDTO.setCreateDateEnd(DateUtils.parseDate(createEnd));

			if (loginBegin != null && loginEnd != null && loginBegin.equals(loginEnd)) {
				loginBegin = loginBegin + " 00:00:00";
				loginEnd = loginEnd + " 23:59:59";
			}
			playerDTO.setLoginDateBegin(DateUtils.parseDate(loginBegin));
			playerDTO.setLoginDateEnd(DateUtils.parseDate(loginEnd));

			// 获取等级范围
			if (playerDTO.getLevel() != null) {
				String[] level = playerDTO.getLevel().split("-");
				playerDTO.setLevelBegin(Integer.valueOf(level[0]));
				playerDTO.setLevelEnd(Integer.valueOf(level[1]));
			}
			// 获取充值范围
			if (playerDTO.getRecharge() != null) {
				String[] recharge = playerDTO.getRecharge().split("-");
				playerDTO.setRechargeBegin(Double.valueOf(recharge[0]));
				playerDTO.setRechargeEnd(Double.valueOf(recharge[1]));
			}
			list = playerMapper.queryForList(playerDTO);
			DataSourceHelper.useDefaultDatabase();
			Iterator<Player> iterator = list.iterator();
			while (iterator.hasNext()) {
				Player player = iterator.next();
				Long playerId = player.getId();
				System.out.println(player.getCombatPower());
				// 通过玩家id获取玩家累充金额
				BigDecimal payAmountSum = payOrderBillMapper.getPayAmountSum(playerId);
				if (payAmountSum == null) {
					payAmountSum = BigDecimal.ZERO;
				}
				double rechargeBegin = 0;
				double rechargeEnd = 0;
				if (playerDTO.getRechargeBegin() != null) {
					rechargeBegin = playerDTO.getRechargeBegin();
					;
				}
				if (playerDTO.getRechargeEnd() != null) {
					rechargeEnd = playerDTO.getRechargeEnd();
				}
				double payAmountSumDouble = payAmountSum.doubleValue();
				PlayerRegisterInfo playerRegisterInfo = playerRegisterInfoMapper.getByPlayerId(playerId);
				if (playerRegisterInfo != null) {
					player.setRegisterTime(playerRegisterInfo.getCreateTime());
				}
				// 获取玩家最后登录时间
				Date loginDate = payUserRankMapper.getPlayerLastLoginTime(playerId, logTable);
				if (loginDate != null) {
					player.setLastLoginTime(loginDate);
				}
				// 充值金额不在这个充值档位范围内,就剔除这条记录
				if (payAmountSumDouble >= rechargeBegin && payAmountSumDouble <= rechargeEnd) {
					player.setPayAmountSum(payAmountSum);
				} else {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			log.error("切换数据源异常,serverId: " + playerDTO.getServerId(), e);
		} finally {
			DataSourceHelper.useDefaultDatabase();
		}
		return list;
	}

	@Override
	public String getNameById(Long playerId) {

		return playerMapper.getNameById(playerId);
	}

	@Override
	public List<PlayerBehavior> queryPlayerBehavior(String rangeDateBegin, String rangeDateEnd, String nickname, int days, int serverId) {
		Date rangeDateBeginTime = null;
		Date rangeDateEndTime = null;
		if (days == 0) {
			rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
			rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
		} else {
			Date nowDate = new Date();
			rangeDateBeginTime = DateUtils.addDays(nowDate, days * (-1));
			rangeDateEndTime = nowDate;
		}
		List<PlayerBehavior> behaviorList = new ArrayList<>();
		List<PlayerRegisterInfo> playerRegisterInfos = null;

		if (!StringUtils.isBlank(nickname)) {
			// 通过玩家昵称模糊匹配获取对应的玩家id
			playerRegisterInfos = playerRegisterInfoMapper.getPlayerByNickname(nickname);
			for (PlayerRegisterInfo playerRegisterInfo : playerRegisterInfos) {
				Long playerId = playerRegisterInfo.getPlayerId();
				String name = playerRegisterInfo.getName();

				// 通过玩家id获取玩家日志信息
				List<PlayerBehavior> list = playerMapper.queryPlayerBehavior(rangeDateBeginTime, rangeDateEndTime, playerId, serverId, logTable);

				// 将list以流的方式通过createDate进行分组变为map
				Map<Date, List<PlayerBehavior>> map = list.stream().collect(Collectors.groupingBy(PlayerBehavior::getCreateDate));
				// 遍历map
				for (List<PlayerBehavior> mqtts : map.values()) {
					PlayerBehavior playerBehavior = new PlayerBehavior();
					// 数据筛选计算
					PlayerBehavior behavior = getBehaviorTreating(mqtts, playerBehavior);
					// 设置日期
					behavior.setCreateDate(mqtts.get(0).getCreateDate());
					behavior.setServerId(serverId);
					behavior.setPlayerId(playerId);
					behavior.setNickname(name);
					behaviorList.add(behavior);
				}
			}
		} else {
			Long playerId = null;
			// 通过玩家id获取玩家日志信息
			List<PlayerBehavior> list = playerMapper.queryPlayerBehavior(rangeDateBeginTime, rangeDateEndTime, playerId, serverId, logTable);

			// 将list以流的方式通过createDate进行分组变为map
			Map<Date, List<PlayerBehavior>> map = list.stream().collect(Collectors.groupingBy(PlayerBehavior::getCreateDate));
			// 遍历map
			//PlayerBehavior playerBehavior = new PlayerBehavior();
			for (List<PlayerBehavior> mqtts : map.values()) {
				PlayerBehavior playerBehavior = new PlayerBehavior();
				// 数据筛选计算
				PlayerBehavior behavior = getBehaviorTreating(mqtts, playerBehavior);
				// 设置日期
				behavior.setCreateDate(mqtts.get(0).getCreateDate());
				behavior.setServerId(serverId);
				behavior.setPlayerId(mqtts.get(0).getPlayerId());
				String name = playerRegisterInfoMapper.getNameByPlayerId(mqtts.get(0).getPlayerId());
				behavior.setNickname(name);
				behaviorList.add(behavior);
			}
		}

		// todo 最后 behaviorList 还需要通过时间做一个排序,map.values()总是一样,查全部和查时间范围内的
		return behaviorList;
	}

	/**
	 * 获取数据处理
	 */
	private PlayerBehavior getBehaviorTreating(List<PlayerBehavior> mqtts, PlayerBehavior behavior) {

		// 数据筛选计算
		long practiceYear = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("修炼年数"))
				.max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
		behavior.setPracticeYear(practiceYear);

		long reamLevel = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("境界等级"))
				.max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
		behavior.setReamLevel(reamLevel);

		long arenaBattle = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("福地夺宝/斗法-挑战")).count();
		behavior.setArenaBattle(arenaBattle);

		long mapExplore = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("仙兽秘境/冒险探索")).count();
		behavior.setMapExplore(mapExplore);

		long mainStoryLevel = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("剧情小关卡"))
				.max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
		behavior.setMainStoryLevel(mainStoryLevel);

		long spiritualWorldoss = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("蛇陵魔窟（灵界器灵）"))
				.max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
		behavior.setSpiritualWorldoss(spiritualWorldoss);

		long worldBoss = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("魔王入侵（世界boss）"))
				.max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
		behavior.setWorldBoss(worldBoss);

		long factionBanquet = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("仙盟仙宴"))
				.max(Comparator.comparing(PlayerBehavior::getValue)).map(PlayerBehavior::getValue).orElse(0L);
		behavior.setFactionBanquet(factionBanquet);

		long recharge = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("当天充值")).mapToLong(PlayerBehavior::getValue).sum();
		behavior.setRecharge(recharge);

		long consumeMoney = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("玉髓消耗")).mapToLong(PlayerBehavior::getValue).sum();
		behavior.setConsumeMoney(consumeMoney);

		long experience = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("阅历值")).mapToLong(PlayerBehavior::getValue).sum();
		behavior.setConsumeMoney(experience);

		long onlineTime = mqtts.stream().filter(i -> i.getType() == PlayerLogType.getType("在线时长")).mapToLong(PlayerBehavior::getValue).sum();
		behavior.setOnlineTime(onlineTime);

		return behavior;
	}
}
