package org.jeecg.modules.game.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jeecg.modules.game.entity.GameRemainStatistisc;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IRemainStatisticsService extends IService<GameRemainStatistisc> {
    /**
     * 查询新增留存数---直接操作数据库方式(缺点，时间跨度大会很慢)(废弃)
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfNewUserlListA(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 查询新增留存数---减少操作数据库方式
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfNewUserlListB(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 查询新增留存数---返回List<JSONObject>
     */
    List<JSONObject> queryRemainStatistiscOfNewUserlListJsonObjectList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName, String daysRange);


    /**
     * 查询首付留存数
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfDownPaymentList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 查询首付留存数---返回List<JSONObject>
     */
    List<JSONObject> queryRemainStatistiscOfDownPaymentListJsonObjectList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName, String daysRange);


    /**
     * 免费留存（废弃）
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfFreeList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 免费留存
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfFreeListB(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName) throws IllegalAccessException, InvocationTargetException, JsonProcessingException, Exception;

    /**
     * 免费留存---返回List<JSONObject>
     */
    List<JSONObject> queryRemainStatistiscOfFreeListBJsonObjectList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName, String daysRange) throws IllegalAccessException, InvocationTargetException, JsonProcessingException, Exception;


    /**
     * 分档位留存（废弃）
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfGradeList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);
    /**
     * 分档位留存
     */
    List<GameRemainStatistisc> queryRemainStatistiscOfGradeListB(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName);

    /**
     * 分档位留存
     */
    List<JSONObject> queryRemainStatistiscOfGradeListBJsonObjectList(String rangeDateBegin, String rangeDateEnd, String tableName, int serverId, String channelName, String daysRange, String grade);
}
