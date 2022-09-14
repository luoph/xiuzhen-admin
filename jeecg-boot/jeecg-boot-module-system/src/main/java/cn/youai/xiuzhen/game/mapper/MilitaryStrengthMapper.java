package cn.youai.xiuzhen.game.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     */
    @Select("select player_id, before_practice_value, reduce_practice_value, after_practice_value, create_time from practice_log where create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') order by create_date desc")
    List<Map> selectPracticeDujieByTimeRange(@Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 查询时间范围内所有注册的用户
     */
    @Select("select account, player_id, name from game_register_info where channel = #{channel} and server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') ")
    List<Map> selectAllRegisterUser(@Param("channel") String channel, @Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 查询时间范围内所有注册的用户
     */
    @Select("select account, player_id, name from game_register_info where server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') ")
    List<Map> selectAllRegisterUserNoChannel(@Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 根据account查询用户
     */
    @Select("select name, account, player_id from game_register_info where account = #{account} and channel = #{channel} and server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') ")
    List<Map> selectRegisterUserByAccount(@Param("account") String account, @Param("channel") String channel, @Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    /**
     * 根据name模糊查询用户
     */
    @Select("select name, account, player_id from game_register_info where channel = #{channel} and server_id = #{serverId} and create_date >= STR_TO_DATE(#{createDateBegin},'%Y-%m-%d') and create_date <= STR_TO_DATE(#{createDateEnd},'%Y-%m-%d') and name like CONCAT('%',#{name},'%') ")
    List<Map> selectRegisterUserByName(@Param("name") String name, @Param("channel") String channel, @Param("serverId") int serverId, @Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

}