package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.*;

import java.util.Date;
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

    /**
     * 统计数据列表
     */
    int GAME_DATA_COUNT_TYPE_DAILY = 1;

    /**
     * 留存统计
     */
    int GAME_DATA_COUNT_TYPE_REMAIN = 2;

    /**
     * ltv统计
     */
    int GAME_DATA_COUNT_TYPE_LTV = 3;

    String KEY_GAME_STAT_REMAIN_COUNT_MAP = "GameStatRemainCountMap";

    String KEY_GAME_STAT_LTV_COUNT_MAP = "GameStatLtvCountMap";

    /**
     * 统计数据列表
     *
     * @param serverId       服务器id
     * @param rangeDateBegin 开始日期
     * @param rangeDateEnd   结束日期
     */
    List<GameStatDaily> queryDateRangeDataCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount);

    /**
     * 按照日期统计每日数据
     */
    GameStatDaily gameDataCount(int serverId, String date);

    List<GameStatRemain> queryDataRemainCount(int serverId, String rangeDateBegin, String rangeDateEnd, boolean isOpenDateCount);

    /**
     * 留存统计
     */
    List<GameStatRemain> queryDataRemainCount(int serverId, String rangeDateBegin, String rangeDateEnd, Date statDate, boolean isOpenDateCount);

    /**
     * 定时统计任务
     * 默认 时间：当天
     * 类型：全类型
     */
    void doJobDataCount();

    /**
     * 定时统计任务 --指定日期
     *
     * @param currentDate 统计日期
     * @param type        2-留存 3-ltv
     */
    void doJobDataCount(Date currentDate, int type);

    /**
     * 留存更新
     */
    void updateRemainTask(Map<String, Object> context, GameServer gameServer, String countDate);


    /**
     * ltv任务更新
     */
    void updateLtvTask(Map<String, Object> context, GameServer gameServer, String countDate);

    /**
     * 30天连续统计
     */
    List<GameStatOngoing> countOngoings();

    /**
     * 30天连续统计--指定日期
     *
     * @param currentDate 指定日期
     * @param types       1-留存 2-ltv
     */
    List<GameStatOngoing> countOngoings(Date currentDate, int[] types);

    /**
     * 30日当天实时查询
     */
    List<GameStatOngoing> queryCountOnGoing(int type, int serverId, String rangeDateBegin, String rangeDateEnd);

    /**
     * 获取k-v 留存列表
     */
    Map<String, GameStatRemain> remainCountMap(boolean isReturnIndex);

    List<GameStatLtv> queryDataLtvCount(int serverId, String rangeDateBegin, String rangeDateEnd, Date statDate);

    Map<String, GameStatLtv> ltvCountMap(boolean isReturnIndex);

    /**
     * 获取k-v ltv数据列表
     * Map<String, GameStatLtv> ltvCountMap(boolean isReturnIndex);
     * <p>
     * /**
     * 按类型-统计数据更新
     */
    void doJobDataCountUpdateByType(int type, Date currentDate);

}
