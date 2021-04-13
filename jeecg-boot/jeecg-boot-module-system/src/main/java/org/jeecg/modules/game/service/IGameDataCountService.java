package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IGameDataCountService
 * @Description 数据统计
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-21 11:05
 */
public interface IGameDataCountService {

	String KEY_GAME_STAT_DAILY_COUNT_MAP = "GameStatDailyCountMap";

	String KEY_GAME_STAT_REMAIN_COUNT_MAP = "GameStatRemainCountMap";

	String KEY_GAME_STAT_LTV_COUNT_MAP = "GameStatLtvCountMap";

	/**
	 * 统计数据列表
	 *
	 * @param gameChannel     渠道id
	 * @param gameServer      服务器id
	 * @param rangeDateBegin  开始日期
	 * @param rangeDateEnd    结束日期
	 * @param isOpenDateCount
	 * @return 统计列表
	 */
	List<GameStatDaily> queryDateRangeDataCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount);

	/**
	 * 统计数据列表
	 *
	 * @param context 上下文
	 */
	List<GameStatDaily> queryDateRangeDataCount(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount);

	/**
	 * 按照日期统计每日数据
	 *
	 * @param gameChannel
	 * @param gameServer
	 * @param date
	 * @return
	 */
	GameStatDaily gameDataCount(GameChannel gameChannel, GameServer gameServer, String date);

	/**
	 * 留存统计
	 *
	 * @param gameChannel
	 * @param gameServer
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @return
	 */
	List<GameStatRemain> queryDataRemainCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount);

	/**
	 * 留存统计
	 *
	 * @param context 上下文
	 */
	List<GameStatRemain> queryDataRemainCount(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount);

	/**
	 * ltv 统计
	 *
	 * @param gameChannel
	 * @param gameServer
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @return
	 */
	List<GameStatLtv> queryDataLtvCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd);

	/**
	 * ltv 统计
	 *
	 * @param context 上下文
	 */
	List<GameStatLtv> queryDataLtvCount(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd);


	/**
	 * 定时统计任务
	 */
	void doJobDataCount();

	/**
	 * 留存更新
	 *
	 * @param gameChannel
	 * @param gameServer
	 * @param countDate
	 */
	void updateRemainTask(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String countDate);


	/**
	 * ltv任务更新
	 *
	 * @param gameChannel
	 * @param gameServer
	 * @param countDate
	 */
	void updateLtvTask(Map<String, Object> context, GameChannel gameChannel, GameServer gameServer, String countDate);


	/**
	 * 统计更新
	 */
	void doJobDataCountUpdate();


	/**
	 * 30天连续统计
	 *
	 * @return
	 */
	List<GameStatOngoing> countOngoings();

	/**
	 * 30日当天实时查询
	 *
	 * @param type
	 * @param gameChannel
	 * @param gameServer
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @return
	 */
	List<GameStatOngoing> queryCountOnGoing(int type, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd);

	/**
	 * 获取k-v 统计数据列表
	 *
	 * @param isReturnIndex 是否只返回索引
	 * @return Map<channel_ServerId_formatDate, GameStatDaily>
	 */
	Map<String, GameStatDaily> dailyCountMap(boolean isReturnIndex);

	/**
	 * 获取k-v 留存列表
	 *
	 * @param isReturnIndex 是否只返回索引
	 * @return Map<channel_ServerId_formatDate, GameStatLtv>
	 */
	Map<String, GameStatRemain> remainCountMap(boolean isReturnIndex);

	/**
	 * 获取k-v ltv数据列表
	 *
	 * @param isReturnIndex 是否只返回索引
	 * @return Map<channel_ServerId_formatDate, GameStatLtv>
	 */
	Map<String, GameStatLtv> ltvCountMap(boolean isReturnIndex);
}
