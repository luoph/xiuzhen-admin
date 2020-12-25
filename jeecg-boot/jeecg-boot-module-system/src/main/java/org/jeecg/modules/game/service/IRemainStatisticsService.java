package org.jeecg.modules.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.game.entity.GameRemainStatistisc;

import java.util.List;

public interface IRemainStatisticsService extends IService<GameRemainStatistisc> {
    /**
     * 查询留存数---直接操作数据库方式(缺点，时间跨度大会很慢)
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfNewUserlListA(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 查询留存数---减少操作数据库方式
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfNewUserlListB(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 查询首付留存数
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfDownPaymentList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 免费留存
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfFreeList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 分档位留存
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfGradeList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);
}
