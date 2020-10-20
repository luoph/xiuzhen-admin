package org.jeecg.modules.player.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.PlayerRegisterInfo;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
public interface IPlayerRegisterInfoService extends IService<PlayerRegisterInfo> {

	/**
	 * 登录流水
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @param playerId
	 * @param serverId
	 * @return
	 */
	List<PlayerRegisterInfo> queryLoginList(String rangeDateBegin, String rangeDateEnd, Long playerId, Integer serverId);
}
