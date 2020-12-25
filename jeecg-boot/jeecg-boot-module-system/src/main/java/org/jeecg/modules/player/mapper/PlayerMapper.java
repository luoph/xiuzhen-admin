package org.jeecg.modules.player.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerBehavior;
import org.jeecg.modules.player.entity.PlayerDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @date 2019-12-14
 */
public interface PlayerMapper extends BaseMapper<Player> {


	List<Player> queryForList(PlayerDTO playerDTO);



	@Select("select nickname from player where id = #{playerId}")
	String getNameById(@Param("playerId") Long playerId);

	List<PlayerBehavior> queryPlayerBehavior(@Param("rangeDateBeginTime") Date rangeDateBeginTime,
	                                         @Param("rangeDateEndTime") Date rangeDateEndTime,
	                                         @Param("playerId") Long playerId,
	                                         @Param("logTable") String logTable);

	/**
	 * 查询所有用户注册信息（唯一不重复）
	 * @return
	 */
	@Select("select player_id, create_time from game_register_info where server_id = #{serverId} order by create_time desc")
	List<Map> selectAllPlayerInfo(@Param("serverId") Integer serverId);
	/**
	 * 查询所有用户登录信息
	 */
	@Select("select player_id, create_time from xiuzhen_log_test.`log_account` where type = 2 and server_id = #{serverId} order by create_time desc")
	List<Map> selectAllLoginInfo(@Param("serverId") Integer serverId);

}
