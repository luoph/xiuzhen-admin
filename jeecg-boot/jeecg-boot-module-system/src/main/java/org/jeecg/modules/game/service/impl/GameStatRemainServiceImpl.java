/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.youai.server.utils.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.constant.RoleType;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.entity.GameStatRemain;
import org.jeecg.modules.game.entity.ServerRemain;
import org.jeecg.modules.game.mapper.GameStatRemainMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.service.IGameStatRemainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
public class GameStatRemainServiceImpl extends ServiceImpl<GameStatRemainMapper, GameStatRemain> implements IGameStatRemainService {

    /**
     * 留存间隔查询
     */
    private static final int[] REMAIN = new int[]{2, 3, 4, 5, 6, 7, 15, 30, 60, 90, 120, 180, 360};

    @Value("${app.log.db}")
    private String logDb;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public void calcRemainStat(RoleType roleType, Collection<Integer> serverIds,
                               Date registerDate, int days, boolean updateAll) {
        String date = DateUtil.formatDate(registerDate);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
                continue;
            }

            LambdaQueryWrapper<GameStatRemain> query = Wrappers.<GameStatRemain>lambdaQuery()
                    .eq(GameStatRemain::getServerId, serverId)
                    .eq(GameStatRemain::getRoleType, roleType.getValue())
                    .eq(GameStatRemain::getCountDate, registerDate);

            // 重新查询注册数量
            GameStatRemain updatedRemain = getBaseMapper().getGameStatRemain(roleType.getValue(), serverId, date);
            GameStatRemain gameStatRemain = getOne(QueryUtils.safeSelectOneQuery(query));
            if (gameStatRemain == null) {
                gameStatRemain = updatedRemain;
            }

            // 付费玩家数量
            gameStatRemain.setRegisterNum(updatedRemain.getRegisterNum());
            // 更新免费玩家数量
            gameStatRemain.setFreeNum(updatedRemain.getRegisterNum() - updatedRemain.getPayNum());

            // 免费、付费留存
            if (days == 2 || updateAll) {
                gameStatRemain.setPayRemain(getBaseMapper().getPayRemain(serverId, date, logDb));
                gameStatRemain.setFreeRemain(getBaseMapper().getFreeRemain(serverId, date, logDb));
            }

            if (updateAll) {
                // 更新全部字段
                for (int i : REMAIN) {
                    if (days < i) {
                        break;
                    }
                    calcRemainAmount(roleType, gameStatRemain, serverId, date, i);
                }
            }
            calcRemainAmount(roleType, gameStatRemain, serverId, date, days);

            if (gameStatRemain.getId() != null) {
                updateById(gameStatRemain);
            } else {
                save(gameStatRemain);
            }
        }
    }

    private void calcRemainAmount(RoleType roleType, GameStatRemain entity, int serverId, String registerDate, int days) {
        int registerNum = entity.getRegisterNum() != null ? entity.getRegisterNum() : 0;
        // 注册为0, 直接返回
        if (registerNum <= 0 || days <= 1 || !ArrayUtil.contains(REMAIN, days)) {
            return;
        }
        ServerRemain serverRemain = null;
        if (roleType == RoleType.ALL) {
            serverRemain = getBaseMapper().selectRemain(serverId, registerDate, days, logDb);
        } else if (roleType == RoleType.PAID) {
            serverRemain = getBaseMapper().selectPayRemain(serverId, registerDate, days, logDb);
        }

        if (serverRemain == null) {
            return;
        }

        if (days <= 2) {
            entity.setD2Remain(serverRemain.getRemain());
        } else if (days <= 3) {
            entity.setD3Remain(serverRemain.getRemain());
        } else if (days <= 4) {
            entity.setD4Remain(serverRemain.getRemain());
        } else if (days <= 5) {
            entity.setD5Remain(serverRemain.getRemain());
        } else if (days <= 6) {
            entity.setD6Remain(serverRemain.getRemain());
        } else if (days <= 7) {
            entity.setD7Remain(serverRemain.getRemain());
        } else if (days <= 15) {
            entity.setD15Remain(serverRemain.getRemain());
        } else if (days <= 30) {
            entity.setD30Remain(serverRemain.getRemain());
        } else if (days <= 60) {
            entity.setD60Remain(serverRemain.getRemain());
        } else if (days <= 90) {
            entity.setD90Remain(serverRemain.getRemain());
        } else if (days <= 120) {
            entity.setD120Remain(serverRemain.getRemain());
        } else if (days <= 180) {
            entity.setD180Remain(serverRemain.getRemain());
        } else if (days <= 360) {
            entity.setD360Remain(serverRemain.getRemain());
        }
    }
}
