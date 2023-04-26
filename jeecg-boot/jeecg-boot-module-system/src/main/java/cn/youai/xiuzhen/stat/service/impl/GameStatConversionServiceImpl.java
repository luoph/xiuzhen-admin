/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.game.service.IGameUserAccountService;
import cn.youai.xiuzhen.stat.entity.GameStatConversion;
import cn.youai.xiuzhen.stat.mapper.GameStatConversionMapper;
import cn.youai.xiuzhen.stat.service.IGameStatConversionService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static cn.youai.enums.AccountLogType.REGISTER;

/**
 * <p>
 * 每日数据统计 服务实现类
 * </p>
 *
 * @author 新增数据（转化统计报表）
 * @since 2020-08-22
 */
@Service
@DS("shardingSphere")
public class GameStatConversionServiceImpl extends ServiceImpl<GameStatConversionMapper, GameStatConversion> implements IGameStatConversionService {

    @Autowired
    private ILogAccountService logAccountService;
    @Autowired
    private IGameUserAccountService userAccountService;

    @Override
    public int updateOrInsert(List<GameStatConversion> list) {
        return getBaseMapper().updateOrInsert(list);
    }

    @Override
    public GameStatConversion getGameStatConversion(String channel, String sdkChannel, Integer serverId, Date date) {
        String configAuth = QueryGenerator.getAllConfigAuth();
        // 新增账号数
        int newAccountNum = userAccountService.queryUserAccountNum(channel, sdkChannel, date, configAuth);
        // 当天注册角色数
        int newPlayerNum = logAccountService.loginRegisterPlayerNum(channel, sdkChannel, serverId, date, REGISTER.getType(), configAuth);
        // 新用户付费角色数
        int newPlayerPayNum = logAccountService.registerPayPlayerNum(channel, sdkChannel, serverId, date, configAuth);
        return GameStatConversion.of(channel, sdkChannel, serverId, date, newAccountNum, newPlayerNum, newPlayerPayNum);
    }

}
