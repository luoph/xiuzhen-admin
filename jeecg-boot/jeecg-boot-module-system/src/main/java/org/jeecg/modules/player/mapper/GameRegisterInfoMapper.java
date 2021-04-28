package org.jeecg.modules.player.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.player.entity.GameRegisterInfo;
import org.jeecg.modules.player.entity.PlayerBehavior;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
public interface GameRegisterInfoMapper extends BaseMapper<GameRegisterInfo> {

    List<GameRegisterInfo> queryLoginList(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
                                          @Param("rangeDateEndTime") Date rangeDateEndTime,
                                          @Param("playerId") Long playerId);


    String queryPlayerIp(@Param("playerId") Long playerId,
                         @Param("createDate") Date createDate,
                         @Param("logTable") String logTable);

    @Select("SELECT * FROM game_register_info WHERE player_id = #{playerId}")
    GameRegisterInfo getByPlayerId(@Param("playerId") Long playerId);

    @Select("SELECT player_id, name FROM game_register_info WHERE name LIKE CONCAT('%',#{nickname},'%') and server_id = #{serverId}")
    List<GameRegisterInfo> getPlayerByNickname(@Param("nickname") String nickname, @Param("serverId") int serverId);

    @Select("SELECT name FROM game_register_info WHERE player_id = #{playerId}")
    String getNameByPlayerId(@Param("playerId") Long playerId);

    @Select("SELECT create_time FROM game_register_info WHERE player_id = #{playerId}")
    Date getCreateTimeByplayerId(Long playerId);

    @Select("SELECT a.* FROM game_register_info a WHERE server_id = #{serverId}")
    List<GameRegisterInfo> getNameByServerId(int serverId);

    List<PlayerBehavior> selectBehaviorCount(@Param("serverId") Integer serverId, @Param("nickname") String nickname, @Param("playerId") Long playerId, @Param("start") Date start, @Param("end") Date end, @Param("logTable") String logTable);
}
