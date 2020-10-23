package org.jeecg.modules.player.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerDTO;

import java.util.List;

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
}
