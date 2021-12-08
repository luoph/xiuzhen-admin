package org.jeecg.modules.player.service.impl;

import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.player.entity.GameRegisterInfo;
import org.jeecg.modules.player.mapper.GameRegisterInfoMapper;
import org.jeecg.modules.player.service.IGameRegisterInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
@Service
public class GameRegisterInfoServiceImpl extends ServiceImpl<GameRegisterInfoMapper, GameRegisterInfo> implements IGameRegisterInfoService {

	@Value("${app.log.db.table}")
	private String logTable;

	@Resource
	private GameRegisterInfoMapper registerInfoMapper;

	@Override
	public List<GameRegisterInfo> queryLoginList(String rangeDateBegin, String rangeDateEnd, Long playerId, Integer serverId) {
		Date rangeDateBeginTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateBegin));
		Date rangeDateEndTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateEnd));
		List<GameRegisterInfo> list = registerInfoMapper.queryLoginList(rangeDateBeginTime, rangeDateEndTime, playerId);
		for (GameRegisterInfo registerInfo : list) {
			Date createDate = registerInfo.getUserOnlineRecord().getCreateDate();
			Long duration = registerInfo.getUserOnlineRecord().getDuration();
			if (duration > 0 && duration != null) {
				registerInfo.getUserOnlineRecord().setDurationMinutes(duration / 60);
			} else {
				registerInfo.getUserOnlineRecord().setDurationMinutes((long) 0);
			}
			// 查询ip
			String ip = registerInfoMapper.queryPlayerIp(playerId, DateUtils.dateOnly(createDate), logTable);
			registerInfo.setIp(ip);

		}
		return list;
	}
}
