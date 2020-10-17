package org.jeecg.modules.player.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.PlayerItemLog;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
public interface IPlayerItemLogService extends IService<PlayerItemLog> {

    /**
     * 设置日志属性
     *
     * @param serverId
     * @param backpacklog
     */
    PlayerItemLog writePlayerItemLog(long serverId, BackpackLog backpacklog);

    /**
     * 保存日志
     *
     * @param logs
     */
    void saveBatchLog(List<PlayerItemLog> logs);

	/**
	 * 查询货币产销
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @param days
	 * @param serverId
	 * @return
	 */
	List<PlayerItemLog> queryCurrencyPayIncomeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId);

	/**
	 * 查询途径分布
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @param days
	 * @param serverId
	 * @param itemId
	 * @param type
	 * @return
	 */
	List<PlayerItemLog> queryWayDistributeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId, int type);
}
