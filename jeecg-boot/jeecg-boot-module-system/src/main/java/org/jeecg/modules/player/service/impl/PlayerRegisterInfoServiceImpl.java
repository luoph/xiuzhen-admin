package org.jeecg.modules.player.service.impl;

import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.player.entity.PlayerRegisterInfo;
import org.jeecg.modules.player.mapper.PlayerRegisterInfoMapper;
import org.jeecg.modules.player.service.IPlayerRegisterInfoService;
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
public class PlayerRegisterInfoServiceImpl extends ServiceImpl<PlayerRegisterInfoMapper, PlayerRegisterInfo> implements IPlayerRegisterInfoService {

	@Value("${app.log.db.table}")
	private String logTable;

	@Resource
	private PlayerRegisterInfoMapper playerRegisterInfoMapper;

	@Override
	public List<PlayerRegisterInfo> queryLoginList(String rangeDateBegin, String rangeDateEnd, Long playerId, Integer serverId) {
		Date rangeDateBeginTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateBegin));
		Date rangeDateEndTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateEnd));
		List<PlayerRegisterInfo> list = playerRegisterInfoMapper.queryLoginList(rangeDateBeginTime, rangeDateEndTime, playerId);
		for (PlayerRegisterInfo playerRegisterInfo : list) {
			Date createDate = playerRegisterInfo.getUserOnlineRecord().getCreateDate();
			Long duration = playerRegisterInfo.getUserOnlineRecord().getDuration();
			if (duration > 0 && duration != null) {
				playerRegisterInfo.getUserOnlineRecord().setDurationMinutes(duration / 60);
			} else {
				playerRegisterInfo.getUserOnlineRecord().setDurationMinutes((long) 0);
			}
			// 查询ip
			String ip = playerRegisterInfoMapper.queryPlayerIp(playerId, DateUtils.dateOnly(createDate), logTable);
			playerRegisterInfo.setIp(ip);

		}
		return list;
	}
}
