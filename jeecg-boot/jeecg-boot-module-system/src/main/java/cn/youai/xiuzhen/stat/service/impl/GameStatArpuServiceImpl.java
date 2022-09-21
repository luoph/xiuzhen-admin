package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.entity.GameStatArpu;
import cn.youai.xiuzhen.stat.mapper.GameStatArpuMapper;
import cn.youai.xiuzhen.stat.service.IGameStatArpuService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description ARPU统计
 * @date 2020-10-10
 */
@Service
@DS("shardingSphere")
public class GameStatArpuServiceImpl extends ServiceImpl<GameStatArpuMapper, GameStatArpu> implements IGameStatArpuService {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameChannelService gameChannelService;

    @Override
    public GameStatArpu selectServerArpu(int serverId, Date countDate) {
        LambdaQueryWrapper<GameStatArpu> query = Wrappers.<GameStatArpu>lambdaQuery()
                .eq(GameStatArpu::getServerId, serverId)
                .eq(GameStatArpu::getChannel, StatisticType.DEFAULT_CHANNEL)
                .eq(GameStatArpu::getCountDate, countDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public GameStatArpu selectChannelArpu(String channel, Date countDate) {
        LambdaQueryWrapper<GameStatArpu> query = Wrappers.<GameStatArpu>lambdaQuery()
                .eq(GameStatArpu::getServerId, 0)
                .eq(GameStatArpu::getChannel, channel)
                .eq(GameStatArpu::getCountDate, countDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public GameStatArpu queryServerArpu(int serverId, Date countDate) {
        return getBaseMapper().queryServerArpu(serverId, countDate);
    }

    @Override
    public GameStatArpu queryChannelArpu(String channel, Date countDate) {
        return getBaseMapper().queryChannelArpu(channel, countDate);
    }

    @Override
    public void calcServerArpu(int serverId, Date countDate) {
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null || gameServer.getOpenTime().after(countDate)) {
            return;
        }

        GameStatArpu updateEntity = queryServerArpu(serverId, countDate).calc();
        GameStatArpu dbEntity = selectServerArpu(serverId, countDate);
        if (dbEntity == null) {
            dbEntity = updateEntity;
            updateEntity.setChannel(StatisticType.DEFAULT_CHANNEL);
        } else {
            dbEntity.apply(updateEntity);
        }

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }

    @Override
    public void calcChannelArpu(String channel, Date countDate) {
        GameChannel gameChannel = gameChannelService.selectChannel(channel);
        if (gameChannel == null) {
            return;
        }

        GameStatArpu updateEntity = queryChannelArpu(channel, countDate).calc();
        GameStatArpu dbEntity = selectChannelArpu(channel, countDate);
        if (dbEntity == null) {
            dbEntity = updateEntity;
            updateEntity.setServerId(StatisticType.DEFAULT_SERVER_ID);
        } else {
            dbEntity.apply(updateEntity);
        }

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }

}
