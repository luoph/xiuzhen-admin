package org.jeecg.modules.game.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author huli
 * @Description: MilitaryStrengthMapper
 * @date 2021/1/4 11:49
 */

public interface MilitaryStrengthMapper {
    /**
     * 查询时间范围内 所有 渡劫 战力变更信息
     * @param createDateBegin
     * @param createDateEnd
     * @return
     */
    @Select("select player_id, before_practice_value, reduce_practice_value, after_practice_value, create_time from practice_log where create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') order by create_date desc")
    List<Map> selectPracticeDujieByTimeRange(@Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 查询时间范围内所有注册的用户
     */
    @Select("select account, player_id, name from game_register_info where server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') ")
    List<Map> selectAllRegisterUser(@Param("channel") String channel, @Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 根据account查询用户
     */
    @Select("select name, account, player_id from game_register_info where account = #{account} and channel = #{channel} and server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') ")
    List<Map> selectRegisterUserByAccount(@Param("account") String account, @Param("channel") String channel, @Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 根据name模糊查询用户
     */
    @Select("select name, account, player_id from game_register_info where server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') and name like CONCAT('%',#{name},'%') ")
    List<Map> selectRegisterUserByName(@Param("name") String name, @Param("channel") String channel, @Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);


    /**
     * 获取所有战力列表
     */
    @Select("select player_id, value, create_time, type, param_1, param_2, param_3 from ${logPlayerTable} where type = 9 and server_id = #{serverId}  and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') order by create_time desc")
    List<Map> selectMilitaryStrengVoAll(@Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd, @Param("logPlayerTable") String logPlayerTable);

    /**
     * 获取某用户某服务器中战力列表（playerId多个，name一个的情况）
     */
    @Select("select player_id, value, create_time, type, param_1, param_2, param_3 from ${logPlayerTable} where player_id in ${playerId} and type = 9 and server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') order by create_time desc")
    List<Map> selectMilitaryStrengVoAllByPlayerId(@Param("serverId") int serverId, @Param("playerId") String playerId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd, @Param("logPlayerTable") String logPlayerTable);
}