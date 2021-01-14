package org.jeecg.modules.game.mapper;

import org.apache.ibatis.annotations.*;
import org.jeecg.modules.game.entity.LogAcount;
import org.jeecg.modules.game.entity.LogPlayer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huli
 * @Description: PlayMethodsTakePartInMapper
 * @date 2021/1/11 14:37
 */

public interface PlayMethodsTakePartInMapper {

    /**
     * 查询玩家日志
     *
     * @param type            日志记录类型
     * @param createDateBegin 开始时间
     * @param createDateEnd   结束时间
     * @param logPlayerTable  表名
     * @param serverId        服务器id
     * @return List<Map < String, Object>>
     */
    @Select({
            "<script>",
            "SELECT player_id, type, create_date, value FROM ${logPlayerTable} where 1=1 ",
            "<if test='type != null and type != \" \" and type != \"\"'>",
            " and type = #{type}",
            "</if>",
            "<if test='createDateBegin != null'>",
            " and create_date &gt;= #{createDateBegin}",
            "</if>",
            "<if test='createDateEnd != null'>",
            " and create_date &lt;= #{createDateEnd}",
            "</if>",
            "<if test='serverId != 0'>",
            " and server_id = #{serverId}",
            "</if> order by create_date",
            "</script>"
    })
    List<LogPlayer> conditionSelectPlayerLog(@Param("type") String type,
                                                       @Param("createDateBegin") Date createDateBegin,
                                                       @Param("createDateEnd") Date createDateEnd,
                                                       @Param("logPlayerTable") String logPlayerTable,
                                                       @Param("serverId") int serverId);

    /**
     * 查询符合等级的用户登录信息
     *
     * @param grade           等级
     * @param logAccountTable 表名
     * @param serverId        服务器名称
     * @return List<Map < String, Object>>
     */
    @Select({
            "<script>",
            "SELECT create_date, value, player_id from ${logAccountTable} where type = 2 and value >= #{grade} ",
            "<if test='serverId != 0'>",
            " and server_id = #{serverId}",
            "</if>",
            "</script>"
    })
    List<LogAcount> selectPlayLoginInfo(@Param("grade") int grade,
                                        @Param("logAccountTable") String logAccountTable,
                                        @Param("serverId") int serverId);
}