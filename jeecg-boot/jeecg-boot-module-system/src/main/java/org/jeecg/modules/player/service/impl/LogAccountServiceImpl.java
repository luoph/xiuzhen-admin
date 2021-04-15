/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.player.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.player.entity.LogAccount;
import org.jeecg.modules.player.mapper.LogAccountMapper;
import org.jeecg.modules.player.service.ILogAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 玩家登录、创角统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-21
 */
@Service
public class LogAccountServiceImpl extends ServiceImpl<LogAccountMapper, LogAccount> implements ILogAccountService {

	@Resource
	private LogAccountMapper logAccountMapper;

	@Value("${app.log.db.table}")
	private String logTable;

	@Override
	public int loginRegisterPlayer(String channel, int serverId, String date, int type) {
		return logAccountMapper.gerLoginRegisterPlayerNum(channel, serverId, date, type, logTable);
	}

	@Override
	public double registerPayAmount(String channel, int serverId, String date) {
		return logAccountMapper.getRegisterPayAmount(channel, serverId, date, logTable);
	}

	@Override
	public int registerPayPlayer(String channel, int serverId, String date) {
		return logAccountMapper.getRegisterPayPlayer(channel, serverId, date, logTable);
	}

	@Override
	public int doublePayRegisterPlayer(String channel, int serverId, String date) {
		return logAccountMapper.getDoublePayRegisterPlayer(channel, serverId, date, logTable);
	}

	@Override
	public List<Long> getPlayerIdsByLoginDate(int serverId, Date date) {
		return logAccountMapper.getPlayerIdsByLoginDate(serverId, DateUtils.formatDate(date, DatePattern.NORM_DATE_PATTERN), logTable);
	}

	@Override
	public List<Long> getPlayerIdsByNoLoginRangeDate(int serverId, Date srcDate, int beforeDate) {
		String startDate = DateUtils.formatDate(DateUtils.minusDays(srcDate, beforeDate), DatePattern.NORM_DATE_PATTERN);
		String endDate = DateUtils.formatDate(srcDate, DatePattern.NORM_DATE_PATTERN);
		return logAccountMapper.getPlayerIdsByNoLoginRangeDate(serverId, startDate, endDate, logTable);
	}
}
