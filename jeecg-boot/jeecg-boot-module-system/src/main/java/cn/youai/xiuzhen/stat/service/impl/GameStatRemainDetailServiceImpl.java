/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.game.constant.RoleType;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.constant.RemainDetailField;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.entity.GameStatRemainDetail;
import cn.youai.xiuzhen.stat.entity.ServerRemain;
import cn.youai.xiuzhen.stat.mapper.GameStatRemainDetailMapper;
import cn.youai.xiuzhen.stat.service.IGameStatRemainDetailService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 留存统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
@Slf4j
@Service
@DS("shardingSphere")
public class GameStatRemainDetailServiceImpl extends ServiceImpl<GameStatRemainDetailMapper, GameStatRemainDetail> implements IGameStatRemainDetailService {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameChannelService gameChannelService;

    @Override
    public GameStatRemainDetail selectServerRemain(int serverId, RoleType roleType, Date registerDate) {
        LambdaQueryWrapper<GameStatRemainDetail> query = Wrappers.<GameStatRemainDetail>lambdaQuery()
                .eq(GameStatRemainDetail::getServerId, serverId)
                .eq(GameStatRemainDetail::getChannel, StatisticType.DEFAULT_CHANNEL)
                .eq(GameStatRemainDetail::getRoleType, roleType.getValue())
                .eq(GameStatRemainDetail::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public GameStatRemainDetail selectChannelRemain(String channel, RoleType roleType, Date registerDate) {
        LambdaQueryWrapper<GameStatRemainDetail> query = Wrappers.<GameStatRemainDetail>lambdaQuery()
                .eq(GameStatRemainDetail::getServerId, 0)
                .eq(GameStatRemainDetail::getChannel, channel)
                .eq(GameStatRemainDetail::getRoleType, roleType.getValue())
                .eq(GameStatRemainDetail::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public void calcServerRemain(int serverId, RoleType roleType, Date registerDate, int days, boolean updateAll) {
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
            return;
        }

        // 重新查询注册数量
        GameStatRemainDetail updateEntity = null;
        if (roleType == RoleType.ALL) {
            updateEntity = getBaseMapper().queryServerRemain(serverId, roleType.getValue(), registerDate);
        } else if (roleType == RoleType.PAID) {
            updateEntity = getBaseMapper().queryServerPayRemain(serverId, roleType.getValue(), registerDate);
        } else if (roleType == RoleType.FREE) {
            updateEntity = getBaseMapper().queryServerFreeRemain(serverId, roleType.getValue(), registerDate);
        }
        assert updateEntity != null;

        GameStatRemainDetail dbEntity = selectServerRemain(serverId, roleType, registerDate);
        if (dbEntity == null) {
            dbEntity = updateEntity;
            updateEntity.setChannel(StatisticType.DEFAULT_CHANNEL);
        } else {
            dbEntity.apply(updateEntity);
        }

        if (updateAll) {
            // 更新全部字段
            for (RemainDetailField value : RemainDetailField.values()) {
                if (days < value.getDays()) {
                    break;
                }
                updateRemainDetailField(dbEntity, serverId, roleType, registerDate, value.getDays());
            }
        }
        updateRemainDetailField(dbEntity, serverId, roleType, registerDate, days);

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }

    @Override
    public void calcChannelRemain(String channel, RoleType roleType, Date registerDate, int days, boolean updateAll) {
        GameChannel gameChannel = gameChannelService.selectChannel(channel);
        if (gameChannel == null) {
            return;
        }

        // 重新查询注册数量
        GameStatRemainDetail updateEntity = null;
        if (roleType == RoleType.ALL) {
            updateEntity = getBaseMapper().queryChannelRemain(channel, roleType.getValue(), registerDate);
        } else if (roleType == RoleType.PAID) {
            updateEntity = getBaseMapper().queryChannelPayRemain(channel, roleType.getValue(), registerDate);
        } else if (roleType == RoleType.FREE) {
            updateEntity = getBaseMapper().queryChannelFreeRemain(channel, roleType.getValue(), registerDate);
        }
        assert updateEntity != null;

        GameStatRemainDetail dbEntity = selectChannelRemain(channel, roleType, registerDate);
        if (dbEntity == null) {
            dbEntity = updateEntity;
            updateEntity.setServerId(StatisticType.DEFAULT_SERVER_ID);
        } else {
            dbEntity.apply(updateEntity);
        }

        if (updateAll) {
            // 更新全部字段
            for (RemainDetailField value : RemainDetailField.values()) {
                if (days < value.getDays()) {
                    break;
                }
                updateRemainDetailField(dbEntity, channel, roleType, registerDate, value.getDays());
            }
        }
        updateRemainDetailField(dbEntity, channel, roleType, registerDate, days);

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }

    private void updateRemainDetailField(GameStatRemainDetail entity, int serverId, RoleType roleType, Date registerDate, int days) {
        RemainDetailField field = RemainDetailField.match(days);
        if (field == null || field.getDays() <= 1) {
            return;
        }

        int baseNum = entity.getD1() != null ? entity.getD1() : 0;
        // 注册为0, 直接返回
        if (baseNum <= 0) {
            field.getFunction().accept(entity, 0);
        } else {
            ServerRemain serverRemain = null;
            if (roleType == RoleType.ALL) {
                serverRemain = getBaseMapper().selectServerRemain(serverId, registerDate, days);
            } else if (roleType == RoleType.PAID) {
                serverRemain = getBaseMapper().selectServerPayRemain(serverId, registerDate, days);
            } else if (roleType == RoleType.FREE) {
                serverRemain = getBaseMapper().selectServerFreeRemain(serverId, registerDate, days);
            }

            int remain = 0;
            if (serverRemain != null && serverRemain.getRemain() != null) {
                remain = serverRemain.getRemain();
            }

            field.getFunction().accept(entity, remain);
        }
    }


    private void updateRemainDetailField(GameStatRemainDetail entity, String channel, RoleType roleType, Date registerDate, int days) {
        RemainDetailField field = RemainDetailField.match(days);
        if (field == null || field.getDays() <= 1) {
            return;
        }

        int baseNum = entity.getD1() != null ? entity.getD1() : 0;
        // 注册为0, 直接返回
        if (baseNum <= 0) {
            field.getFunction().accept(entity, 0);
        } else {
            ServerRemain serverRemain = null;
            if (roleType == RoleType.ALL) {
                serverRemain = getBaseMapper().selectChannelRemain(channel, registerDate, days);
            } else if (roleType == RoleType.PAID) {
                serverRemain = getBaseMapper().selectChannelPayRemain(channel, registerDate, days);
            } else if (roleType == RoleType.FREE) {
                serverRemain = getBaseMapper().selectChannelFreeRemain(channel, registerDate, days);
            }

            int remain = 0;
            if (serverRemain != null && serverRemain.getRemain() != null) {
                remain = serverRemain.getRemain();
            }

            field.getFunction().accept(entity, remain);
        }
    }
}
