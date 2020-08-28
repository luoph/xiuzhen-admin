package org.jeecg.modules.game.service;

import cn.youai.commons.model.ResponseCode;
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
     * 统计参数效验
     *
     * @param channelId      渠道id
     * @param serverId       服务器id
     * @param rangeDateBegin 统计开始时间
     * @param rangeDateEnd   统计结束时间
     * @return 状态
     */
    boolean isParamValidCheck(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd);

    /**
     * 统计数据列表
     *
     * @param channelId      渠道id
     * @param serverId       服务器id
     * @param rangeDateBegin 开始日期
     * @param rangeDateEnd   结束日期
     * @return 统计列表
     */
    List<GameDayDataCount> queryDateRangeDataCount(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd);

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
     * @param channelId
     * @param serverId
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @return
     */
    List<GameDataRemain> queryDataRemainCount(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd);

    /**
     * ltv 统计
     *
     * @param channelId
     * @param serverId
     * @param rangeDateBegin
     * @param rangeDateEnd
     * @return
     */
    List<GameLtvCount> queryDataLtvCount(int channelId, int serverId, String rangeDateBegin, String rangeDateEnd);


    void doJobDataCount();
}
