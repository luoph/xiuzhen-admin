package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.constant.CoreStatisticType;
import cn.youai.xiuzhen.game.constant.RoleType;
import cn.youai.xiuzhen.game.entity.GameChannelServer;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import cn.youai.xiuzhen.stat.service.IGameDataCountService;
import cn.youai.xiuzhen.stat.service.IGameStatLtvDetailService;
import cn.youai.xiuzhen.stat.service.IGameStatRemainDetailService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName IGameDataCountServiceImpl
 * @Description 数据统计业务实现
 * @Author buliangliang
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020-08-21 11:07
 */
@Service
@Slf4j
public class GameDataCountServiceImpl implements IGameDataCountService {

    @Autowired
    private IGameChannelServerService gameChannelServerService;

    @Autowired
    private IGameStatRemainDetailService statRemainDetailService;
    @Autowired
    private IGameStatLtvDetailService statLtvDetailService;

    @Override
    public void doJobDataCount() {
//        doJobDataCount(DateUtils.todayDate(), CoreStatisticType.REMAIN);
        doJobDataCount(DateUtils.todayDate(), CoreStatisticType.LTV);
    }

    @Override
    public void doJobDataCount(Date registerDate, CoreStatisticType type) {
        Wrapper<GameChannelServer> query = Wrappers.<GameChannelServer>lambdaQuery().eq(GameChannelServer::getDelFlag, 0).eq(GameChannelServer::getNoNeedCount, 0);
        List<GameChannelServer> list = gameChannelServerService.list(query);
        Map<Integer, GameChannelServer> serverMap = list.stream().collect(Collectors.toMap(GameChannelServer::getServerId, Function.identity(), (key1, key2) -> key2));

        int days = DateUtils.daysBetweenNatural(registerDate, DateUtils.now());
        if (days < 0) {
            return;
        }

        switch (type) {
            case LTV:
                statLtvDetailService.calcLtvDetailStat(serverMap.keySet(), serverMap.keySet(), registerDate, days + 1, true);
                break;

            case REMAIN_DETAIL:
                statRemainDetailService.calcRemainDetailStat(RoleType.ALL, serverMap.keySet(), registerDate, days, true);
                statRemainDetailService.calcRemainDetailStat(RoleType.PAID, serverMap.keySet(), registerDate, days, true);
                statRemainDetailService.calcRemainDetailStat(RoleType.FREE, serverMap.keySet(), registerDate, days, true);
                break;

            default:
                break;
        }
    }

    @Override
    public void doJobDataCountUpdateByType(CoreStatisticType type, Date current) {
        // TODO 处理ltv、留存每日更新任务
    }
}
