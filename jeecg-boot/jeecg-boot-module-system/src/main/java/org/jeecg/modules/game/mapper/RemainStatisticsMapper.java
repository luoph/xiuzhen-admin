package org.jeecg.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.game.entity.GameRemainStatistisc;

import java.util.List;
import java.util.Map;

/**
 * author:huli
 * description:留存统计mapper
 */
public interface RemainStatisticsMapper extends BaseMapper<GameRemainStatistisc> {
    /**
     * 时间段内存在于 注册表 也存在于 登录表 的数量
     *
     * @param tableName
     * @param registerBeginTime
     * @param registerEndTime
     * @param channelName
     * @param serverId
     * @param loginBeginTime
     * @param loginEndTime
     * @return
     */
    @Select("select count(*) as ramainSum from (" +
            "SELECT count(*) FROM game_register_info a LEFT JOIN ${tableName} l " +
            "ON l.`player_id` = a.`player_id` AND l.`channel` = a.`channel` AND l.`server_id` = a.`server_id` " +
            "WHERE l.`create_time` >= STR_TO_DATE(#{loginBeginTime},'%Y-%m-%d %H:%i:%s') " +
            "AND l.`create_time` < STR_TO_DATE(#{loginEndTime},'%Y-%m-%d %H:%i:%s') " +
            "AND a.`create_time` >= STR_TO_DATE(#{registerBeginTime},'%Y-%m-%d %H:%i:%s') " +
            "AND a.`create_time` < STR_TO_DATE(#{registerEndTime},'%Y-%m-%d %H:%i:%s') " +
            "AND a.`channel` = #{channelName} AND a.`server_id` = #{serverId} AND l.`channel` = #{channelName}" +
            "AND l.`server_id` = #{serverId} GROUP BY a.player_id" +
            ") b")
    String selectnNwUserRemainSum(@Param("tableName") String tableName, @Param("registerBeginTime") String registerBeginTime,
                                  @Param("registerEndTime") String registerEndTime, @Param("channelName") String channelName,
                                  @Param("serverId") int serverId, @Param("loginBeginTime") String loginBeginTime, @Param("loginEndTime") String loginEndTime);


    /**
     * 查询时间范围内所有注册的用户
     */
    @Select("select account,create_date,player_id from game_register_info where channel = #{channel} and server_id = #{serverId} and create_date >= STR_TO_DATE(#{creatTimeBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{creatTimeEnd},'%Y-%m-%d') ")
    List<Map> selectAllRegisterUser(@Param("channel") String channel, @Param("serverId") int serverId, @Param("creatTimeBegin") String creatTimeBegin, @Param("creatTimeEnd") String creatTimeEnd);

    /**
     * 查询时间范围内所有登陆操作的用户
     */
    @Select("select account,create_date,player_id from ${tableName} where channel = #{channel} and server_id = #{serverId} and create_date >= STR_TO_DATE(#{creatTimeBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{creatTimeEnd},'%Y-%m-%d') ")
    List<Map> selectAllLoginAndRegisterUser(@Param("channel") String channel, @Param("serverId") int serverId, @Param("tableName") String tableName, @Param("creatTimeBegin") String creatTimeBegin, @Param("creatTimeEnd") String creatTimeEnd);

    /**
     * 查询时间范围内所有已支付订单表
     */
    @Select("select player_id,pay_amount,create_time from game_order where channel = #{channelName} and server_id = #{serverId} and  order_status >= 1 and create_time >= STR_TO_DATE(#{creatTimeBegin},'%Y-%m-%d %H:%i:%s') and create_time <= STR_TO_DATE(#{creatTimeEnd},'%Y-%m-%d %H:%i:%s') ")
    List<Map> selectAllOrderHasPayList(@Param("channelName") String channelName, @Param("serverId") int serverId, @Param("creatTimeBegin") String creatTimeBegin, @Param("creatTimeEnd") String creatTimeEnd);

    /**
     * 查询时间范围内所有未支付订单表
     */
    @Select("select player_id,pay_amount,create_time from game_order where channel = #{channelName} and server_id = #{serverId} and  order_status = 0 and create_time >= STR_TO_DATE(#{creatTimeBegin},'%Y-%m-%d %H:%i:%s') and create_time <= STR_TO_DATE(#{creatTimeEnd},'%Y-%m-%d %H:%i:%s') ")
    List<Map> selectAllOrderNoPayList(@Param("channelName") String channelName, @Param("serverId") int serverId, @Param("creatTimeBegin") String creatTimeBegin, @Param("creatTimeEnd") String creatTimeEnd);

}
