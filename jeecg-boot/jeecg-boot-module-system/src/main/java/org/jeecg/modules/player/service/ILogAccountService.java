/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.player.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.player.entity.LogAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 玩家登录、创角统计 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-21
 */
public interface ILogAccountService extends IService<LogAccount> {
	/**
	 * 登录注册角色数
	 *
	 * @param channel
	 * @param serverId
	 * @param date
	 * @param type
	 * @return
	 */
	int loginRegisterPlayer(String channel, int serverId, String date, int type);

	/**
	 * 新注册付费总额
	 *
	 * @param channel
	 * @param serverId
	 * @param date
	 * @return
	 */
	double registerPayAmount(String channel, int serverId, String date);

	/**
	 * 新注册付费玩家
	 *
	 * @param channel
	 * @param serverId
	 * @param date
	 * @return
	 */
	int registerPayPlayer(String channel, int serverId, String date);

	/**
	 * 二次付费玩家
	 *
	 * @param channel
	 * @param serverId
	 * @param date
	 * @return
	 */
	int doublePayRegisterPlayer(String channel, int serverId, String date);

	/**
	 * 当前登陆玩家ids
	 *
	 * @param serverId
	 * @param date
	 * @return
	 */
	List<Long> getPlayerIdsByLoginDate(int serverId, Date date);

	/**
	 * 指定日期没登陆玩家id
	 *
	 * @param serverId
	 * @param srcDate    目标日期
	 * @param beforeDate 指定前几天
	 * @return
	 */
	List<Long> getPlayerIdsByNoLoginRangeDate(int serverId, Date srcDate, int beforeDate);
}
