/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import cn.youai.xiuzhen.stat.mapper.GameStatRemainMapper;
import cn.youai.xiuzhen.stat.service.IGameStatRemainService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * LTV统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Service
@DS("shardingSphere")
public class GameStatRemainServiceImpl extends ServiceImpl<GameStatRemainMapper, GameStatRemain> implements IGameStatRemainService {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameChannelService gameChannelService;

    @Override
    public GameStatRemain queryServerRemain(int serverId, Date registerDate) {
        return getBaseMapper().queryServerRemain(serverId, registerDate);
    }

    @Override
    public GameStatRemain queryChannelRemain(String channel, Date registerDate) {
        return getBaseMapper().queryChannelRemain(channel, registerDate);
    }

    @Override
    public GameStatRemain selectServerRemain(int serverId, Date registerDate) {
        LambdaQueryWrapper<GameStatRemain> query = Wrappers.<GameStatRemain>lambdaQuery()
                .eq(GameStatRemain::getServerId, serverId)
                .eq(GameStatRemain::getChannel, StatisticType.DEFAULT_CHANNEL)
                .eq(GameStatRemain::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public GameStatRemain selectChannelRemain(String channel, Date registerDate) {
        LambdaQueryWrapper<GameStatRemain> query = Wrappers.<GameStatRemain>lambdaQuery()
                .eq(GameStatRemain::getServerId, 0)
                .eq(GameStatRemain::getChannel, channel)
                .eq(GameStatRemain::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public void calcServerRemain(int serverId, Date registerDate) {
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
            return;
        }

        // 重新查询注册数量
        GameStatRemain updateEntity = queryServerRemain(serverId, registerDate);
        GameStatRemain dbEntity = selectServerRemain(serverId, registerDate);

        // 免费玩家数 = 注册数 - 付费数
        updateEntity.setFreeNum(updateEntity.getRegisterNum() - updateEntity.getPayNum());
        if (dbEntity == null) {
            dbEntity = updateEntity;
            updateEntity.setChannel(StatisticType.DEFAULT_CHANNEL);
        } else {
            // 付费玩家数量
            dbEntity.setRegisterNum(updateEntity.getRegisterNum());
            dbEntity.setPayNum(updateEntity.getPayNum());
            dbEntity.setFreeNum(updateEntity.getFreeNum());
            dbEntity.setPayRemain(updateEntity.getPayRemain());
            dbEntity.setFreeRemain(updateEntity.getFreeRemain());
            dbEntity.setRegisterRemain(updateEntity.getRegisterRemain());
        }

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }

    @Override
    public void calcChannelRemain(String channel, Date registerDate) {
        GameChannel gameChannel = gameChannelService.selectChannel(channel);
        if (gameChannel == null) {
            return;
        }

        // 重新查询注册数量
        GameStatRemain updatedEntity = queryChannelRemain(channel, registerDate);
        GameStatRemain dbEntity = selectChannelRemain(channel, registerDate);

        // 免费玩家数 = 注册数 - 付费数
        updatedEntity.setFreeNum(updatedEntity.getRegisterNum() - updatedEntity.getPayNum());
        if (dbEntity == null) {
            dbEntity = updatedEntity;
            updatedEntity.setServerId(StatisticType.DEFAULT_SERVER_ID);
        } else {
            // 付费玩家数量
            dbEntity.setRegisterNum(updatedEntity.getRegisterNum());
            dbEntity.setPayNum(updatedEntity.getPayNum());
            dbEntity.setFreeNum(updatedEntity.getFreeNum());
            dbEntity.setPayRemain(updatedEntity.getPayRemain());
            dbEntity.setFreeRemain(updatedEntity.getFreeRemain());
            dbEntity.setRegisterRemain(updatedEntity.getRegisterRemain());
        }

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }
}
