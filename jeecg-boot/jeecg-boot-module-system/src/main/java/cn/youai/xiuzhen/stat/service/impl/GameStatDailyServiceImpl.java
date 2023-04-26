/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.entity.GameStatDaily;
import cn.youai.xiuzhen.stat.mapper.GameStatDailyMapper;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import cn.youai.xiuzhen.stat.service.IGameStatDailyService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static cn.youai.enums.AccountLogType.LOGIN;
import static cn.youai.enums.AccountLogType.REGISTER;

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
    public GameStatDaily getGameStatDaily(String channel, String sdkChannel, Integer serverId, Date date) {
        String configAuth = QueryGenerator.getAllConfigAuth();
        // 当天登录角色数
        int loginNum = logAccountService.loginRegisterPlayerNum(channel, sdkChannel, serverId, date, LOGIN.getType(), configAuth);
        // 支付玩家数
        int payPlayerNum = payOrderService.payPlayerNum(channel, sdkChannel, serverId, date, configAuth);
        // 当天付费总金额
        BigDecimal payAmount = payPlayerNum <= 0 ? BigDecimal.ZERO : payOrderService.payAmount(channel, sdkChannel, serverId, date, configAuth);
        // 当天注册角色数
        int registerPlayerNum = logAccountService.loginRegisterPlayerNum(channel, sdkChannel, serverId, date, REGISTER.getType(), configAuth);
        // 注册付费总金额
        BigDecimal registerPayAmount = registerPlayerNum <= 0 ? BigDecimal.ZERO : logAccountService.registerPayAmount(channel, sdkChannel, serverId, date, configAuth);
        // 注册付费玩家
        int registerPayPlayerNum = registerPlayerNum <= 0 ? 0 : logAccountService.registerPayPlayerNum(channel, sdkChannel, serverId, date, configAuth);
        // 注册二次付费玩家
        int doublePayPlayer = registerPlayerNum <= 0 ? 0 : logAccountService.doublePayRegisterPlayerNum(channel, sdkChannel, serverId, date, configAuth);
        return GameStatDaily.of(channel, sdkChannel, serverId, date, payAmount, payPlayerNum, loginNum, registerPlayerNum, registerPayAmount, registerPayPlayerNum, doublePayPlayer);
    }

}
