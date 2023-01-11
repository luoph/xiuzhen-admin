/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.enums.AccountLogType;
import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import cn.youai.xiuzhen.stat.mapper.GameStatDailyMapper;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import cn.youai.xiuzhen.stat.service.IGameStatDailyService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 每日数据统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
@Service
@DS("shardingSphere")
public class GameStatDailyServiceImpl extends ServiceImpl<GameStatDailyMapper, GameStatDaily> implements IGameStatDailyService {

    @Autowired
    private IGameOrderService payOrderService;

    @Autowired
    private ILogAccountService logAccountService;

    @Override
    public int updateOrInsert(List<GameStatDaily> list) {
        return getBaseMapper().updateOrInsert(list);
    }

    @Override
    public GameStatDaily getGameStatDaily(String channel, Integer serverId, Date date) {
        // 当天付费总金额
        BigDecimal payAmount = payOrderService.channelPayAmount(channel, serverId, date);
        // 支付玩家数
        int payPlayerNum = payOrderService.channelPayPlayerNum(channel, serverId, date);
        // 当天登录角色数
        int loginNum = logAccountService.channelLoginRegisterPlayerNum(channel, serverId, date, AccountLogType.LOGIN.getType());
        // 当天注册角色数
        int registerPlayer = logAccountService.channelLoginRegisterPlayerNum(channel, serverId, date, AccountLogType.REGISTER.getType());
        // 注册付费总金额
        BigDecimal registerPayAmount = logAccountService.channelRegisterPayAmount(channel, serverId, date);
        // 注册付费玩家
        int registerPayPlayer = logAccountService.channelRegisterPayPlayerNum(channel, serverId, date);
        // 注册二次付费玩家
        int doublePayPlayer = logAccountService.channelDoublePayRegisterPlayer(channel, serverId, date);
        return GameStatDaily.of(channel, serverId, date, payAmount, payPlayerNum, loginNum, registerPlayer, registerPayAmount, registerPayPlayer, doublePayPlayer);
    }

}
