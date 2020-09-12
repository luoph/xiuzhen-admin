package org.jeecg.modules.game.service;

import org.jeecg.modules.game.entity.*;

import java.util.List;

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
     * @param gameChannel    渠道id
     * @param gameServer     服务器id
     * @param rangeDateBegin 开始日期
     * @param rangeDateEnd   结束日期
     * @return 统计列表
     */
    List<GameDayDataCount> queryDateRangeDataCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd);

    /**
     * 按照日期统计每日数据
     *
     * @param gameChannel
     * @param gameServer
     * @param date
     * @return
     */
    GameDayDataCount gameDataCount(GameChannel gameChannel, GameServer gameServer, String date);

    /**
     * 留存统计
     *
     * @param gameChannel
     * @param gameServer
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @return
     */
    List<GameDataRemain> queryDataRemainCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd);

    /**
     * ltv 统计
     *
     * @param gameChannel
     * @param gameServer
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @return
     */
    List<GameLtvCount> queryDataLtvCount(GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd);


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
    void updateRemainTask(GameChannel gameChannel, GameServer gameServer, String countDate);


    /**
     * ltv任务更新
     *
     * @param gameChannel
     * @param gameServer
     * @param countDate
     */
    void updateLtvTask(GameChannel gameChannel, GameServer gameServer, String countDate);


    /**
     * 统计更新
     */
    void doJobDataCountUpdate();


    /**
     * 30天连续统计
     *
     * @return
     */
    List<GameCountOngoing> countOngoings();

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
    List<GameCountOngoing> queryCountOnGoing(int type, GameChannel gameChannel, GameServer gameServer, String rangeDateBegin, String rangeDateEnd);
}
