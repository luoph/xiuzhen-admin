package org.jeecg.modules.player.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.Player;
import org.jeecg.modules.player.entity.PlayerBehavior;
import org.jeecg.modules.player.entity.PlayerDTO;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家信息
 * @date 2019-12-14
 */
public interface IPlayerService extends IService<Player> {

	/**
	 * 玩家查询
	 *
	 * @param playerDTO
	 * @return
	 */
	List<Player> queryForList(PlayerDTO playerDTO);

	/**
	 * 查询玩家姓名
	 *
	 * @param playerId
	 * @return
	 */
	String getNameById(Long playerId);

	/**
	 * 查询玩家行为
	 *
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @param nickname
	 * @param days
	 * @param serverId
	 * @return
	 */
	List<PlayerBehavior> queryPlayerBehavior(String rangeDateBegin, String rangeDateEnd, String nickname, int days, int serverId);
}
