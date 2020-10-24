package org.jeecg.modules.game.service.impl;

import cn.youai.commons.model.DataResponse;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.okhttp.OkHttpHelper;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameChannelServer;
import org.jeecg.modules.game.entity.GameOnlineNum;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.mapper.GameOnlineNumMapper;
import org.jeecg.modules.game.service.IGameChannelServerService;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameOnlineNumService;
import org.jeecg.modules.game.service.IGameServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 在线情况
 * @date 2020-10-15
 */
@Service
public class GameOnlineNumServiceImpl extends ServiceImpl<GameOnlineNumMapper, GameOnlineNum> implements IGameOnlineNumService {

	private static final Type RESPONSE_ONLINE_NUM = new TypeReference<DataResponse<Integer>>() {
	}.getType();

	@Value("${app.online-num-url:/game/onlineNum}")
	private String onlineNumUrl;

	@Value("${app.log.db.table}")
	private String logTable;

	@Autowired
	private IGameChannelServerService gameChannelServerService;
	@Autowired
	private IGameChannelService gameChannelService;
	@Autowired
	private IGameServerService gameServerService;

	@Resource
	private GameOnlineNumMapper gameOnlineNumMapper;

	@Override
	public void doGameOnlineNumSave() {
		try {
			List<GameChannelServer> list = gameChannelServerService.list();
			list = list.stream().filter(gameChannelServer -> gameChannelServer.getDelFlag() == 0 && gameChannelServer.getNoNeedCount() == 0).collect(Collectors.toList());
			for (GameChannelServer gameChannelServer : list) {
				GameServer gameServer = gameServerService.getById(gameChannelServer.getServerId());
				GameChannel gameChannel = gameChannelService.getById(gameChannelServer.getChannelId());
				// 获取服务器id和服务器名字
				Integer serverId = gameServer.getId();
				String channel = gameChannel.getSimpleName();

				if (gameServer.getOnlineStat() == 1) {
					// http调用查询在线人数
					DataResponse<Integer> response = JSON.parseObject(OkHttpHelper.get(gameServer.getGmUrl() + onlineNumUrl), RESPONSE_ONLINE_NUM);
					GameOnlineNum gameOnlineNum = new GameOnlineNum();
					// 不为空就保存数据
					long data = 0;
					if (response != null) {
						// 获取在线人数
						data = response.getData();
					}
					//http调用如果 response为空,查不到就设置为0
					gameOnlineNum.setChannel(channel);
					gameOnlineNum.setServerId(serverId);
					gameOnlineNum.setOnlineNum(data);
					gameOnlineNum.setGetTime(new Date());
					gameOnlineNum.setUpdateTime(new Date());
					gameOnlineNum.setCreateTime(new Date());

					save(gameOnlineNum);
				}
			}
		} catch (Exception e) {
			log.error("定时器调用GameOnlineNumJob: 查询在线人数异常", e);
		}
	}

	@Override
	public List<GameOnlineNum> queryGameOnlineNumByRangDate(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
		if (days == 0) {
			Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
			Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
			return gameOnlineNumMapper.queryGameOnlineNumByRangDate(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
		}
		//如果有选天数,就使用就近天数查询
		//获取过去第几天的日期
		Date nowDate = new Date();
		Date pastDate = DateUtils.addDays(nowDate, days * (-1));
		return gameOnlineNumMapper.queryGameOnlineNumByRangDate(pastDate, nowDate, serverId, channel);
	}

	@Override
	public List<GameOnlineNum> queryGameOnlineCollectByRangDate(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
		if (days == 0) {
			Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
			Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
			return getDataTreating(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
		}
		//如果有选天数,就使用就近天数查询
		//获取过去第几天的日期
		Date nowDate = new Date();
		Date pastDate = DateUtils.addDays(nowDate, days * (-1));
		return getDataTreating(pastDate, nowDate, serverId, channel);
	}


	/**
	 * 获取数据处理
	 *
	 * @param rangeDateBeginTime
	 * @param rangeDateEndTime
	 * @param serverId
	 * @param channel
	 * @return
	 */
	private List<GameOnlineNum> getDataTreating(Date rangeDateBeginTime, Date rangeDateEndTime, Integer serverId, String channel) {
		List<GameOnlineNum> gameOnlineNumList = gameOnlineNumMapper.queryGameOnlineCollectByRangDate(rangeDateBeginTime, rangeDateEndTime, serverId, channel);
		for (GameOnlineNum gameOnlineNum : gameOnlineNumList) {
			Date getTime = gameOnlineNum.getGetTime();
			gameOnlineNum.setDau(gameOnlineNumMapper.queryDau(getTime, logTable));
		}
		return gameOnlineNumList;
	}
}