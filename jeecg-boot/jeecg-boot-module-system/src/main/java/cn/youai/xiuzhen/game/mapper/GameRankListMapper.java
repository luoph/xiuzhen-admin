package cn.youai.xiuzhen.game.mapper;

import cn.youai.xiuzhen.game.entity.GameRegisterInfo;
import cn.youai.xiuzhen.game.entity.LogAccount;
import cn.youai.xiuzhen.game.entity.LogPlayer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author huli
 */
public interface GameRankListMapper {
    /**
     * 查询用户行为日志
     */
    @Select("select player_id, value,create_time from `log_account` " +
            "where type = #{type} and server_id = #{serverId} " +
            "and create_date <= #{createTime} order by value")
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
    @Select("select `player_id`, `value` from `log_account` where `type` = 2")
    List<LogAccount> selectLogAccount();

    /**
     * 查询log_player中type>=100的类型行为总数
     */
    @Select("select player_id, count(*) as value from `log_player` " +
            "where type = #{type} and server_id = #{serverId} " +
            "and create_time <= #{createTime} " +
            "group by player_id having count(*) >= 1 " +
            "ORDER BY count(*) desc")
    List<LogPlayer> selectCount(@Param("type") int type,
                                @Param("createTime") Date createTime,
                                @Param("serverId") int serverId);

    /**
     * 查询log_player中type<100的类型行为最大值
     */
    @Select("select player_id, max(value) as value from `log_player` " +
            "where type = #{type} and server_id = #{serverId} " +
            "and create_time <= #{createTime} " +
            "group by player_id ORDER BY max(value) desc")
    List<LogPlayer> selectSum(@Param("type") int type,
                              @Param("createTime") Date createTime,
                              @Param("serverId") int serverId);

}
