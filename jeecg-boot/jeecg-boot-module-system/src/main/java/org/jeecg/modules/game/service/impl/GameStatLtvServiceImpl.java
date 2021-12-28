/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.entity.GameStatLtv;
import org.jeecg.modules.game.entity.ServerLtvAmount;
import org.jeecg.modules.game.mapper.GameStatLtvMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.service.IGameStatLtvService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GameStatLtvServiceImpl extends ServiceImpl<GameStatLtvMapper, GameStatLtv> implements IGameStatLtvService {
    /**
     * LTV统计间隔
     */
    private static final int[] LTV = new int[]{1, 2, 3, 4, 5, 6, 7, 14, 21, 30, 60, 90, 120, 180, 360};

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public void calcLtvStat(Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll) {
        String date = DateUtil.formatDate(registerDate);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
                continue;
            }

            LambdaQueryWrapper<GameStatLtv> query = Wrappers.<GameStatLtv>lambdaQuery()
                    .eq(GameStatLtv::getServerId, serverId)
                    .eq(GameStatLtv::getCountDate, registerDate);

            // 重新查询注册数量
            GameStatLtv updatedLtv = getBaseMapper().getGameStatLtv(serverId, date);
            GameStatLtv gameStatLtv = getOne(QueryUtils.safeSelectOneQuery(query));
            if (gameStatLtv == null) {
                gameStatLtv = updatedLtv;
            } else {
                gameStatLtv.setRegisterNum(updatedLtv.getRegisterNum());
            }

            if (updateAll) {
                // 更新全部字段
                for (int i : LTV) {
                    if (days < i) {
                        break;
                    }
                    calcLtvAmount(gameStatLtv, serverId, date, i);
                }
            }
            calcLtvAmount(gameStatLtv, serverId, date, days);

            if (gameStatLtv.getId() != null) {
                updateById(gameStatLtv);
            } else {
                save(gameStatLtv);
            }
        }
    }

    private void calcLtvAmount(GameStatLtv entity, int serverId, String registerDate, int days) {
        int registerNum = entity.getRegisterNum() != null ? entity.getRegisterNum() : 0;
        // 注册为0, 直接返回
        if (registerNum <= 0) {
            return;
        }

        ServerLtvAmount ltvAmount = getBaseMapper().getLtvAmount(serverId, registerDate, days);
        if (days <= 1) {
            entity.setD1Amount(ltvAmount.getTotalAmount());
        } else if (days <= 2) {
            entity.setD2Amount(ltvAmount.getTotalAmount());
        } else if (days <= 3) {
            entity.setD3Amount(ltvAmount.getTotalAmount());
        } else if (days <= 4) {
            entity.setD4Amount(ltvAmount.getTotalAmount());
        } else if (days <= 5) {
            entity.setD5Amount(ltvAmount.getTotalAmount());
        } else if (days <= 6) {
            entity.setD6Amount(ltvAmount.getTotalAmount());
        } else if (days <= 7) {
            entity.setD7Amount(ltvAmount.getTotalAmount());
        } else if (days <= 14) {
            entity.setD14Amount(ltvAmount.getTotalAmount());
        } else if (days <= 21) {
            entity.setD21Amount(ltvAmount.getTotalAmount());
        } else if (days <= 30) {
            entity.setD30Amount(ltvAmount.getTotalAmount());
        } else if (days <= 60) {
            entity.setD60Amount(ltvAmount.getTotalAmount());
        } else if (days <= 90) {
            entity.setD90Amount(ltvAmount.getTotalAmount());
        } else if (days <= 120) {
            entity.setD120Amount(ltvAmount.getTotalAmount());
        } else if (days <= 180) {
            entity.setD180Amount(ltvAmount.getTotalAmount());
        } else if (days <= 360) {
            entity.setD360Amount(ltvAmount.getTotalAmount());
        }
    }
}
