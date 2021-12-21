package org.jeecg.modules.game.service;

import org.jeecg.modules.game.constant.CoreStatisticType;
import org.jeecg.modules.game.entity.GameStatDaily;
import org.jeecg.modules.game.entity.GameStatRemain;

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
     * @param registerDate 统计日期
     * @param type         2-留存 3-ltv
     */
    void doJobDataCount(Date registerDate, CoreStatisticType type);

    /**
     * 获取k-v 留存列表
     */
    Map<String, GameStatRemain> remainCountMap(boolean isReturnIndex);

    /**
     * 获取k-v ltv数据列表
     * Map<String, GameStatLtv> ltvCountMap(boolean isReturnIndex);
     * <p>
     * /**
     * 按类型-统计数据更新
     */
    void doJobDataCountUpdateByType(CoreStatisticType type, Date currentDate);

}
