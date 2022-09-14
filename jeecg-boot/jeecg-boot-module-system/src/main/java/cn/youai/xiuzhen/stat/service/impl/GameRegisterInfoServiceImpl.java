package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameRegisterInfo;
import cn.youai.xiuzhen.game.mapper.GameRegisterInfoMapper;
import cn.youai.xiuzhen.game.service.IGameRegisterInfoService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家注册信息
 * @date 2020-01-04
 */
@Service
@DS("master")
public class GameRegisterInfoServiceImpl extends ServiceImpl<GameRegisterInfoMapper, GameRegisterInfo> implements IGameRegisterInfoService {

    @Autowired
    private ILogAccountService logAccountService;

    @Override
    public List<GameRegisterInfo> queryLoginList(String rangeDateBegin, String rangeDateEnd, Long playerId, Integer serverId) {
        Date rangeDateBeginTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateBegin));
        Date rangeDateEndTime = DateUtils.dateOnly(DateUtils.parseDate(rangeDateEnd));
        List<GameRegisterInfo> list = getBaseMapper().queryLoginList(rangeDateBeginTime, rangeDateEndTime, playerId);
        for (GameRegisterInfo registerInfo : list) {
            Date createDate = registerInfo.getUserOnlineRecord().getCreateDate();
            Long duration = registerInfo.getUserOnlineRecord().getDuration();
            if (duration > 0 && duration != null) {
                registerInfo.getUserOnlineRecord().setDurationMinutes(duration / 60);
            } else {
                registerInfo.getUserOnlineRecord().setDurationMinutes((long) 0);
            }
            // 查询ip
            String ip = logAccountService.queryPlayerIp(playerId, DateUtils.dateOnly(createDate));
            registerInfo.setIp(ip);

        }
        return list;
    }
}
