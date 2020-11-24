package org.jeecg.modules.player.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.player.entity.PlayerBehavior;
import org.jeecg.modules.player.entity.PlayerRegisterInfo;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
public interface PlayerRegisterInfoMapper extends BaseMapper<PlayerRegisterInfo> {

    List<PlayerRegisterInfo> queryLoginList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                            @Param("rangeDateEndTime") Date rangeDateEndTime,
                                            @Param("playerId") Long playerId);


    String queryPlayerIp(@Param("playerId") Long playerId,
                         @Param("createDate") Date createDate,
                         @Param("logTable") String logTable);

    @Select("SELECT * FROM player_register_info WHERE player_id = #{playerId}")
    PlayerRegisterInfo getByPlayerId(@Param("playerId") Long playerId);

    @Select("SELECT player_id, name FROM player_register_info WHERE name LIKE CONCAT('%',#{nickname},'%') and server_id = #{serverId}")
    List<PlayerRegisterInfo> getPlayerByNickname(@Param("nickname") String nickname, @Param("serverId") int serverId);

    @Select("SELECT name FROM player_register_info WHERE player_id = #{playerId}")
    String getNameByPlayerId(@Param("playerId") Long playerId);

    @Select("SELECT create_time FROM player_register_info WHERE player_id = #{playerId}")
    Date getCreateTimeByplayerId(Long playerId);

    @Select("SELECT a.* FROM player_register_info a WHERE server_id = #{serverId}")
    List<PlayerRegisterInfo> getNameByServerId(int serverId);

    List<PlayerBehavior> selectBehaviorCount(long serverId, String nickname, Date start, Date end, String logTable);
}
