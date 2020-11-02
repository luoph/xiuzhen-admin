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
import org.jeecg.modules.player.entity.*;
import org.jeecg.modules.player.mapper.PlayerMapper;
import org.jeecg.modules.player.mapper.PlayerRegisterInfoMapper;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Value("${app.log.db.table_log_player}")
	private String logTable;

	@Override
	public List<Player> queryForList(PlayerDTO playerDTO) {
		List<Player> list = new ArrayList<Player>();

		// 四个stream流临时转换变量
		List<Player> collect1 = new ArrayList<Player>();
		List<Player> collect2 = new ArrayList<Player>();
		List<Player> collect3 = new ArrayList<Player>();
		List<Player> collect4 = new ArrayList<Player>();

		try {
			// 如果选择开始时间和结束时间是同一天
			String createBegin = playerDTO.getCreateBegin();
			String createEnd = playerDTO.getCreateEnd();
			String loginBegin = playerDTO.getLoginBegin();
			String loginEnd = playerDTO.getLoginEnd();
			// 时间不为空
			// todo 时间为同一天,之后需要把这种时间代码替换成工具类的方法
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
			if (playerDTO.getLevel() != null && !"".equals(playerDTO.getLevel())) {
				String[] level = playerDTO.getLevel().split("-");
				playerDTO.setLevelBegin(Integer.valueOf(level[0]));
				playerDTO.setLevelEnd(Integer.valueOf(level[1]));
			}
			// 获取充值范围
			if (playerDTO.getRecharge() != null && !"".equals(playerDTO.getRecharge())) {
				String[] recharge = playerDTO.getRecharge().split("-");
				playerDTO.setRechargeBegin(Double.valueOf(recharge[0]));
				playerDTO.setRechargeEnd(Double.valueOf(recharge[1]));
			}
			DataSourceHelper.useServerDatabase(playerDTO.getServerId());
			list = playerMapper.queryForList(playerDTO);
			DataSourceHelper.useDefaultDatabase();
			// 通过玩家id获取玩家累充金额
			List<PayOrder> payOrders = payOrderBillMapper.getPayAmountSum(playerDTO.getServerId());
			List<Player> playerTimes = payUserRankMapper.getPlayerLastLoginAndRegisterTime(playerDTO.getServerId(), logTable);

			for (Player player : list) {
				Long playerId = player.getId();
				BigDecimal orderSum = getOrderSum(player, payOrders);
				// 设置支付总金额
				player.setPayAmountSum(orderSum);
				// 获取玩家注册时间,获取玩家最后登录时间
				for (Player playerTime : playerTimes) {
					if (playerId.equals(playerTime.getId())) {
						if (playerTime.getRegisterTime() != null) {
							player.setRegisterTime(playerTime.getRegisterTime());
						}
						if (playerTime.getLastLoginTime() != null) {
							player.setLastLoginTime(playerTime.getLastLoginTime());
						}
						break;
					}
				}
			}

			if (playerDTO.getCreateDateBegin() != null && playerDTO.getCreateDateEnd() != null && list != null) {

				collect1 = list.stream().filter(t -> t.getRegisterTime() != null && t.getRegisterTime().getTime() >= playerDTO.getCreateDateBegin().getTime()
						&& t.getRegisterTime().getTime() <= playerDTO.getCreateDateEnd().getTime()).collect(Collectors.toList());
			} else {
				collect1 = list;
			}

			if (playerDTO.getLoginDateBegin() != null && playerDTO.getLoginDateEnd() != null && collect1 != null) {
				collect2 = collect1.stream().filter(t -> t.getLastLoginTime() != null && t.getLastLoginTime().getTime() >= playerDTO.getLoginDateBegin().getTime()
						&& t.getRegisterTime().getTime() <= playerDTO.getLoginDateEnd().getTime()).collect(Collectors.toList());
			} else {
				collect2 = collect1;
			}

			if (playerDTO.getRechargeBegin() != null && playerDTO.getRechargeEnd() != null && collect2 != null) {
				collect3 = collect2.stream().filter(t -> t.getPayAmountSum() != null && t.getPayAmountSum().doubleValue() >= playerDTO.getRechargeBegin()
						&& t.getPayAmountSum().doubleValue() <= playerDTO.getRechargeEnd()).collect(Collectors.toList());
			} else {
				collect3 = collect2;
			}

			if (playerDTO.getLevelBegin() != null && playerDTO.getLevelEnd() != null && collect3 != null) {
				collect4 = collect3.stream().filter(t -> t.getLevel() != null && t.getLevel() >= playerDTO.getLevelBegin()
						&& t.getLevel() <= playerDTO.getLevelEnd()).collect(Collectors.toList());
				collect4 = collect3;
			} else {
				collect4 = collect3;
			}

		} catch (Exception e) {
			log.error("切换数据源异常,serverId: " + playerDTO.getServerId(), e);
		} finally {
			DataSourceHelper.useDefaultDatabase();
		}
		return collect4;
	}

	private BigDecimal getOrderSum(Player player, List<PayOrder> payOrders) {
		double sum = payOrders.stream().filter(o -> o.getPlayerId().equals(player.getId())).mapToDouble(o -> o.getPayAmount().doubleValue()).sum();
		return BigDecimal.valueOf(sum);
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
			Map<Long, List<PlayerBehavior>> map1 = list.stream().collect(Collectors.groupingBy(PlayerBehavior::getPlayerId));
			map1.forEach((k, v) -> {
				Map<Date, List<PlayerBehavior>> map2 = v.stream().collect(Collectors.groupingBy(PlayerBehavior::getCreateDate));
				map2.forEach((k2, v2) -> {
					PlayerBehavior playerBehavior = new PlayerBehavior();
					// 数据筛选计算
					PlayerBehavior behavior = getBehaviorTreating(v2, playerBehavior);
					// 设置日期
					behavior.setCreateDate(v2.get(0).getCreateDate());
					behavior.setServerId(serverId);
					behavior.setPlayerId(v2.get(0).getPlayerId());
					String name = playerRegisterInfoMapper.getNameByPlayerId(v2.get(0).getPlayerId());
					behavior.setNickname(name);
					behaviorList.add(behavior);
				});
			});
		}
		// todo 最后 behaviorList 还需要通过时间做一个排序
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
