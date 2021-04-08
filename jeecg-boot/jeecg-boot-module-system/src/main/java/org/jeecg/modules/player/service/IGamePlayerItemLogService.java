package org.jeecg.modules.player.service;

import cn.youai.xiuzhen.entity.pojo.ConfItem;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.player.entity.BackpackLog;
import org.jeecg.modules.player.entity.GamePlayerItemLog;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description player_item_log
 * @date 2020-07-21
 */
public interface IGamePlayerItemLogService extends IService<GamePlayerItemLog> {

	/**
	 * 设置日志属性
	 *
	 * @param serverId
	 * @param backpacklog
	 */
	GamePlayerItemLog writePlayerItemLog(Integer serverId, BackpackLog backpacklog);

	/**
	 * 保存日志
	 *
	 * @param logs
	 */
	void saveBatchLog(List<GamePlayerItemLog> logs);

	/**
	 * 查询货币产销
	 *
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @param days
	 * @param serverId
	 * @return
	 */
	List<GamePlayerItemLog> queryCurrencyPayIncomeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId);

	/**
	 * 查询途径分布
	 *
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @param days
	 * @param serverId
	 * @param itemId
	 * @param type
	 * @return
	 */
	List<GamePlayerItemLog> queryWayDistributeList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, int itemId, int type);

	/**
	 * 查询物品流水
	 *
	 * @param rangeDateBegin
	 * @param rangeDateEnd
	 * @param way
	 * @param serverId
	 * @param itemId
	 * @param type
	 * @return
	 */
	List<GamePlayerItemLog> queryItemBillList(String rangeDateBegin, String rangeDateEnd, int way, Integer serverId, int itemId, int type, Long playerId);

	/**
	 * 获得静态道具列表
	 *
	 * @param itemId
	 * @param itemName
	 * @return
	 */
	List<ConfItem> getConfItemList(Integer itemId, String itemName);
}
