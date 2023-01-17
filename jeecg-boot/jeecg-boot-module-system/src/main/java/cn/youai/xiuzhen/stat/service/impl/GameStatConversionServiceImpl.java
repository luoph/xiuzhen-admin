/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.enums.AccountLogType;
import cn.youai.xiuzhen.game.service.IGameUserAccountService;
import cn.youai.xiuzhen.stat.entity.GameStatConversion;
import cn.youai.xiuzhen.stat.mapper.GameStatConversionMapper;
import cn.youai.xiuzhen.stat.service.IGameStatConversionService;
import cn.youai.xiuzhen.stat.service.ILogAccountService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        int newAccountNum = userAccountService.userAccountNum(channel, sdkChannel, date);
        // 当天注册角色数
        int newPlayerNum = logAccountService.channelLoginRegisterPlayerNum(channel, sdkChannel, serverId, date, AccountLogType.REGISTER.getType());
        // 新用户付费角色数
        int newPlayerPayNum = logAccountService.channelRegisterPayPlayerNum(channel, sdkChannel, serverId, date);
        return GameStatConversion.of(channel, sdkChannel, serverId, date, newAccountNum, newPlayerNum, newPlayerPayNum);
    }

}
