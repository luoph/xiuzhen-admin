package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.entity.pojo.OrderStatus;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.PayUserRank;
import org.jeecg.modules.game.entity.PlayerRegisterInfoVO;
import org.jeecg.modules.game.mapper.PayUserRankMapper;
import org.jeecg.modules.game.service.IPayUserRankService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Service
public class PayUserRankServiceImpl extends ServiceImpl<PayUserRankMapper, PayUserRank> implements IPayUserRankService {

	@Resource
	private PayUserRankMapper payUserRankMapper;

	@Value("${app.log.db.table}")
	private String logTable;

	@Override
	public List<PayUserRank> queryUserRankByDateRange(String payTimeBegin, String payTimeEnd, Integer serverId, String channel) {

		Date payTimeBeginDate = DateUtils.parseDate(payTimeBegin);
		Date payTimeEndDate = DateUtils.parseDate(payTimeEnd);

		return payUserRankMapper.queryUserRankByDateRange(payTimeBeginDate, payTimeEndDate, serverId, channel);
	}

	@Override
	public List<PayUserRank> queryPayRankByDateRange(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
		List<PayUserRank> list = null;
		Date nowDate = new Date();
		if (days == 0) {
			Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
			Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
			list = payUserRankMapper.queryPayRankByDateRange(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
			return getDataTreating(list);
		}
		//如果有选天数,就使用就近天数查询
		//获取过去第几天的日期
		Date pastDate = DateUtils.addDays(nowDate, days * (-1));
		list = payUserRankMapper.queryPayRankByDateRange(pastDate, nowDate, serverId, channel);
		return getDataTreating(list);
	}

	/**
	 * 获取数据处理后的list
	 *
	 * @param list
	 * @return
	 */
	public List<PayUserRank> getDataTreating(List<PayUserRank> list) {
		Date nowDate = new Date();
		for (PayUserRank payUserRank : list) {
			//获取玩家注册信息
			PlayerRegisterInfoVO playerRegisterInfo = payUserRank.getPlayerRegisterInfo();

			//获取玩家最后的充值时间
			Date payTimeMax = payUserRank.getPayTimeMax();

			int payWarningDays = DateUtils.daysBetween(payTimeMax, nowDate);
			//设置充值预警天数
			playerRegisterInfo.setPayWarningDays(payWarningDays);

			//获取玩家最后登录时间和注册时间
			Date loginDate = payUserRankMapper.getPlayerLastLoginTime(payUserRank.getPlayerId(), logTable);

			int loginWarningDays = DateUtils.daysBetween(loginDate, nowDate);
			//设置最后登录时间
			playerRegisterInfo.setLoginDate(loginDate);
			//设置登录预警天数
			playerRegisterInfo.setLoginWarningDays(loginWarningDays);

			payUserRank.setPlayerRegisterInfo(playerRegisterInfo);
		}
		return list;
	}

	/**
	 * 获取订单状态的枚举Map
	 *
	 * @return
	 */
	private Map getOrderStatusMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String paid = OrderStatus.PAID.getName();
		String forward = OrderStatus.FORWARD.getName();
		String issuing = OrderStatus.ISSUING.getName();
		String complete = OrderStatus.COMPLETE.getName();
		map.put(paid, OrderStatus.PAID.getType());
		map.put(forward, OrderStatus.PAID.getType());
		map.put(issuing, OrderStatus.PAID.getType());
		map.put(complete, OrderStatus.PAID.getType());
		return map;
	}
}
