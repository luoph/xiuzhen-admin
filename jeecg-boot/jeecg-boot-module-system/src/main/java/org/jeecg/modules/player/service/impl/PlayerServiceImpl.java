package org.jeecg.modules.player.service.impl;

import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.mapper.PayOrderGiftMapper;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerDTO;
import org.jeecg.modules.player.mapper.PlayerMapper;
import org.jeecg.modules.player.service.IPlayerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @ 2019-12-14
 */
@Slf4j
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements IPlayerService {

	private static final String[] LEVEL = {"0-30", "30-60", "60-100"};
	private static final String[] RECHARGE = {"0-6", "6-30", "30-100"};

	@Resource
	private PlayerMapper playerMapper;

	@Resource
	private PayOrderBillMapper payOrderBillMapper;

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
			playerDTO.setCreateDateBegin(DateUtils.dateOnly(DateUtils.parseDate(createBegin)));
			playerDTO.setCreateDateEnd(DateUtils.dateOnly(DateUtils.parseDate(createEnd)));

			if (loginBegin != null && loginEnd != null && loginBegin.equals(loginEnd)) {
				loginBegin = loginBegin + " 00:00:00";
				loginEnd = loginEnd + " 23:59:59";
			}
			playerDTO.setLoginDateBegin(DateUtils.dateOnly(DateUtils.parseDate(loginBegin)));
			playerDTO.setLoginDateEnd(DateUtils.dateOnly(DateUtils.parseDate(loginEnd)));

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
				// 通过玩家id获取玩家累充金额
				BigDecimal payAmountSum = payOrderBillMapper.getPayAmountSum(playerId);
				if (payAmountSum == null) {
					payAmountSum = BigDecimal.ZERO;
				}
				double rechargeBegin = playerDTO.getRechargeBegin();
				double rechargeEnd = playerDTO.getRechargeEnd();
				double payAmountSumDouble = payAmountSum.doubleValue();
				if (payAmountSumDouble > rechargeBegin && payAmountSumDouble < rechargeEnd) {
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
}
