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
import cn.youai.xiuzhen.stat.constant.RemainField;
import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import cn.youai.xiuzhen.stat.entity.ServerRemain;
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
    public GameStatRemain queryServerRemain(int serverId, int roleType, Date registerDate) {
        return getBaseMapper().queryServerRemain(serverId, roleType, registerDate);
    }

    @Override
    public GameStatRemain queryChannelRemain(String channel, int roleType, Date registerDate) {
        return getBaseMapper().queryChannelRemain(channel, roleType, registerDate);
    }

    @Override
    public GameStatRemain selectServerRemain(int serverId, RoleType roleType, Date registerDate) {
        LambdaQueryWrapper<GameStatRemain> query = Wrappers.<GameStatRemain>lambdaQuery()
                .eq(GameStatRemain::getServerId, serverId)
                .eq(GameStatRemain::getChannel, "default")
                .eq(GameStatRemain::getRoleType, roleType.getValue())
                .eq(GameStatRemain::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public GameStatRemain selectChannelRemain(String channel, RoleType roleType, Date registerDate) {
        LambdaQueryWrapper<GameStatRemain> query = Wrappers.<GameStatRemain>lambdaQuery()
                .eq(GameStatRemain::getServerId, 0)
                .eq(GameStatRemain::getChannel, channel)
                .eq(GameStatRemain::getRoleType, roleType.getValue())
                .eq(GameStatRemain::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public void calcServerRemain(int serverId, RoleType roleType, Date registerDate, int days, boolean updateAll) {
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
            return;
        }

        // 重新查询注册数量
        GameStatRemain updatedRemain = queryServerRemain(serverId, roleType.getValue(), registerDate);
        GameStatRemain gameStatRemain = selectServerRemain(serverId, roleType, registerDate);

        // 免费玩家数 = 注册数 - 付费数
        updatedRemain.setFreeNum(updatedRemain.getRegisterNum() - updatedRemain.getPayNum());
        if (gameStatRemain == null) {
            gameStatRemain = updatedRemain;
            updatedRemain.setChannel("default");
        } else {
            // 付费玩家数量
            gameStatRemain.setRegisterNum(updatedRemain.getRegisterNum());
            gameStatRemain.setPayNum(updatedRemain.getPayNum());
            gameStatRemain.setFreeNum(updatedRemain.getFreeNum());
            gameStatRemain.setPayRemain(updatedRemain.getPayRemain());
            gameStatRemain.setFreeRemain(updatedRemain.getFreeRemain());
        }

        if (updateAll) {
            // 更新全部字段
            for (RemainField value : RemainField.values()) {
                if (days < value.getDays()) {
                    break;
                }
                calcRemainAmount(roleType, gameStatRemain, serverId, registerDate, value.getDays());
            }
        }

        if (RemainField.valueOf(days) == null) {
            calcRemainAmount(roleType, gameStatRemain, serverId, registerDate, days);
        }

        if (gameStatRemain.getId() != null) {
            updateById(gameStatRemain);
        } else {
            save(gameStatRemain);
        }
    }

    @Override
    public void calcChannelRemain(String channel, RoleType roleType, Date registerDate, int days, boolean updateAll) {
        GameChannel gameChannel = gameChannelService.selectChannel(channel);
        if (gameChannel == null) {
            return;
        }

        // 重新查询注册数量
        GameStatRemain updatedRemain = queryChannelRemain(channel, roleType.getValue(), registerDate);
        GameStatRemain gameStatRemain = selectChannelRemain(channel, roleType, registerDate);

        // 免费玩家数 = 注册数 - 付费数
        updatedRemain.setFreeNum(updatedRemain.getRegisterNum() - updatedRemain.getPayNum());
        if (gameStatRemain == null) {
            gameStatRemain = updatedRemain;
            updatedRemain.setServerId(0);
        } else {
            // 付费玩家数量
            gameStatRemain.setRegisterNum(updatedRemain.getRegisterNum());
            gameStatRemain.setPayNum(updatedRemain.getPayNum());
            gameStatRemain.setFreeNum(updatedRemain.getFreeNum());
            gameStatRemain.setPayRemain(updatedRemain.getPayRemain());
            gameStatRemain.setFreeRemain(updatedRemain.getFreeRemain());
        }

        if (updateAll) {
            // 更新全部字段
            for (RemainField value : RemainField.values()) {
                if (days < value.getDays()) {
                    break;
                }
                calcRemainAmount(roleType, gameStatRemain, channel, registerDate, value.getDays());
            }
        }

        if (RemainField.valueOf(days) == null) {
            calcRemainAmount(roleType, gameStatRemain, channel, registerDate, days);
        }

        if (gameStatRemain.getId() != null) {
            updateById(gameStatRemain);
        } else {
            save(gameStatRemain);
        }
    }

    private void calcRemainAmount(RoleType roleType, GameStatRemain entity, int serverId, Date registerDate, int days) {
        int registerNum = entity.getRegisterNum() != null ? entity.getRegisterNum() : 0;
        if (days <= 1) {
            return;
        }

        // 注册为0, 直接返回
        ServerRemain serverRemain = null;
        if (registerNum <= 0) {
            serverRemain = new ServerRemain().setRemain(0).setServerId(serverId).setDays(days).setRegisterDate(registerDate);
        } else {
            if (roleType == RoleType.ALL) {
                serverRemain = getBaseMapper().selectServerPlayerRemain(serverId, registerDate, days);
            } else if (roleType == RoleType.PAID) {
                serverRemain = getBaseMapper().selectServerPayPlayerRemain(serverId, registerDate, days);
            } else if (roleType == RoleType.FREE) {
                serverRemain = getBaseMapper().selectServerFreePlayerRemain(serverId, registerDate, days);
            }
        }

        if (serverRemain == null) {
            return;
        }

        RemainField remainField = RemainField.match(days);
        if (remainField != null) {
            remainField.getFunction().accept(entity, serverRemain.getRemain());
        }
    }

    private void calcRemainAmount(RoleType roleType, GameStatRemain entity, String channel, Date registerDate, int days) {
        int registerNum = entity.getRegisterNum() != null ? entity.getRegisterNum() : 0;
        if (days <= 1) {
            return;
        }

        // 注册为0, 直接返回
        ServerRemain serverRemain = null;
        if (registerNum <= 0) {
            serverRemain = new ServerRemain().setRemain(0).setChannel(channel).setDays(days).setRegisterDate(registerDate);
        } else {
            if (roleType == RoleType.ALL) {
                serverRemain = getBaseMapper().selectChannelPlayerRemain(channel, registerDate, days);
            } else if (roleType == RoleType.PAID) {
                serverRemain = getBaseMapper().selectChannelPayPlayerRemain(channel, registerDate, days);
            } if (roleType == RoleType.FREE) {
                serverRemain = getBaseMapper().selectChannelFreePlayerRemain(channel, registerDate, days);
            }
        }

        if (serverRemain == null) {
            return;
        }

        RemainField remainField = RemainField.match(days);
        if (remainField != null) {
            remainField.getFunction().accept(entity, serverRemain.getRemain());
        }
    }
}
