package org.jeecg.modules.game.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.game.entity.LogAccount;
import org.jeecg.modules.game.entity.LogPlayer;
import org.jeecg.modules.player.entity.GameRegisterInfo;

import java.util.Date;
import java.util.List;

/**
 * @author huli
 */
public interface GameRankListMapper {
    /**
     * 查询用户行为日志
     *
     * @param logPlayerTable
     * @param type
     * @param createTime
     * @param serverId
     * @return
     */
    @Select("select player_id, value,create_time from ${logPlayerTable} where type = #{type} and server_id = #{serverId} and  create_date <= #{createTime} order by value")
    List<LogPlayer> selectLogPlayer(@Param("logPlayerTable") String logPlayerTable,
                                    @Param("type") int type,
                                    @Param("createTime") Date createTime,
                                    @Param("serverId") int serverId);
    /**
     * 查询用户角色信息
     */
    @Select("select player_id, name from game_register_info")
    List<GameRegisterInfo> selectRegisterInfo();

    /**
     * 查询用户登录等级
     */
    @Select("select player_id, value from ${logAcountTable} where type = 2")
    List<LogAccount> selectLogAccount(@Param("logAcountTable") String logAcountTable);
}
