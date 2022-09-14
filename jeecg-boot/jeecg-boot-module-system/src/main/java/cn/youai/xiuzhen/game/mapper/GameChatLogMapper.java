package cn.youai.xiuzhen.game.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author huli
 * @version 1.0.0
 */
public interface GameChatLogMapper {
    /**
     * 世界：根据条件查询 用户发送的世界信息信息
     * 【需要根据服务器切换数据源】
     * param:时间段、玩家id、发送的信息
     */
    @Select({
            "<script>",
            "SELECT player_id,message,create_time FROM chat_message where 1=1 ",
            "<if test='playerId != null and playerId != \" \" and playerId != \"\"'>",
            " and player_id = #{playerId}",
            "</if>",
            "<if test='rangeTimeBegin != null and rangeTimeBegin != \" \" and rangeTimeBegin != \"\"'>",
            " and create_time &gt;= STR_TO_DATE(#{rangeTimeBegin},'%Y-%m-%d %H:%i:%s')",
            "</if>",
            "<if test='rangeTimeEnd != null and rangeTimeEnd != \" \" and rangeTimeEnd != \"\"'>",
            " and create_time &lt; STR_TO_DATE(#{rangeTimeEnd},'%Y-%m-%d %H:%i:%s')",
            "</if>",
            "<if test='message != null and message != \" \" and message != \"\"'>",
            " and message like '%${message}%'",
            "</if> order by create_time desc",
            "</script>"
    })
    List<Map> selectWorldMassageByAllKindOf(@Param("playerId") Long playerId,
                                            @Param("message") String message,
                                            @Param("rangeTimeBegin") String rangeTimeBegin,
                                            @Param("rangeTimeEnd") String rangeTimeEnd);

    /**
     * 查询所有用户信息
     * 【需要根据服务器切换数据源】
     */
    @Select("select id,account,nickname from player")
    List<Map> selectPlayerInfoByPlayerId();

    /**
     * 查询所有仙盟群聊会话配置
     * 【需要根据服务器切换数据源】
     */
    @Select("select channel_id, player_id from friend_chat_channel where status = 1 and type = 2")
    List<Map> selectAllImmortalChatChannel();

    /**
     * 查询所有私聊会话配置
     * 【需要根据服务器切换数据源】
     */
    @Select("select channel_id, player_id from friend_chat_channel where status = 1 and type = 1")
    List<Map> selectAllSelfChatChannel();

    /**
     * 仙盟：根据条件查询 用户发送的仙盟信息
     * 【需要根据服务器切换数据源】
     * param:时间段、玩家id、发送的信息
     */
    @Select({
            "<script>",
            "SELECT channel_id,player_id,message,create_time FROM friend_chat_message where 1=1 ",
            "<if test='playerId != null and playerId != \" \" and playerId != \"\"'>",
            " and player_id = #{playerId}",
            "</if>",
            "<if test='rangeTimeBegin != null and rangeTimeBegin != \" \" and rangeTimeBegin != \"\"'>",
            " and create_time &gt;= STR_TO_DATE(#{rangeTimeBegin},'%Y-%m-%d %H:%i:%s')",
            "</if>",
            "<if test='rangeTimeEnd != null and rangeTimeEnd != \" \" and rangeTimeEnd != \"\"'>",
            " and create_time &lt; STR_TO_DATE(#{rangeTimeEnd},'%Y-%m-%d %H:%i:%s')",
            "</if>",
            "<if test='message != null and message != \" \" and message != \"\"'>",
            " and message like '%${message}%'",
            "</if> order by create_time desc",
            "</script>"
    })
    List<Map> selectImmortalMessageByAllKindOf(@Param("playerId") Long playerId,
                                               @Param("message") String message,
                                               @Param("rangeTimeBegin") String rangeTimeBegin,
                                               @Param("rangeTimeEnd") String rangeTimeEnd);

    /**
     * 私聊：根据条件查询 用户发送的私聊信息信息
     * 【需要根据服务器切换数据源】
     * param:时间段、玩家id、发送的信息
     */
    @Select({
            "<script>",
            "SELECT channel_id,player_id,message,create_time FROM friend_chat_message where 1=1 ",
            "<if test='playerId != null and playerId != \" \" and playerId != \"\"'>",
            " and player_id = #{playerId}",
            "</if>",
            "<if test='rangeTimeBegin != null and rangeTimeBegin != \" \" and rangeTimeBegin != \"\"'>",
            " and create_time &gt;= STR_TO_DATE(#{rangeTimeBegin},'%Y-%m-%d %H:%i:%s')",
            "</if>",
            "<if test='rangeTimeEnd != null and rangeTimeEnd != \" \" and rangeTimeEnd != \"\"'>",
            " and create_time &lt; STR_TO_DATE(#{rangeTimeEnd},'%Y-%m-%d %H:%i:%s')",
            "</if>",
            "<if test='message != null and message != \" \" and message != \"\"'>",
            " and message like '%${message}%'",
            "</if> order by create_time desc",
            "</script>"
    })
    List<Map> selectSelfMessageByAllKindOf(@Param("playerId") Long playerId,
                                           @Param("message") String message,
                                           @Param("rangeTimeBegin") String rangeTimeBegin,
                                           @Param("rangeTimeEnd") String rangeTimeEnd);
}
