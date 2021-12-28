/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.constant.LtvDetailField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.entity.GameStatLtvDetail;
import org.jeecg.modules.game.entity.ServerLtvAmount;
import org.jeecg.modules.game.mapper.GameStatLtvDetailMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.service.IGameStatLtvDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * LTV统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
@Service
public class GameStatLtvDetailServiceImpl extends ServiceImpl<GameStatLtvDetailMapper, GameStatLtvDetail> implements IGameStatLtvDetailService {

    @Autowired
    private IGameServerService gameServerService;

    @Override
    public void calcLtvDetailStat(Set<Integer> keySet, Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll) {
        String date = DateUtil.formatDate(registerDate);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
                continue;
            }

            LambdaQueryWrapper<GameStatLtvDetail> query = Wrappers.<GameStatLtvDetail>lambdaQuery()
                    .eq(GameStatLtvDetail::getServerId, serverId)
                    .eq(GameStatLtvDetail::getCountDate, registerDate);

            // 重新查询注册数量
            GameStatLtvDetail updatedLtv = getBaseMapper().getGameStatLtvDetail(serverId, date);
            GameStatLtvDetail entity = getOne(QueryUtils.safeSelectOneQuery(query));
            if (entity == null) {
                entity = updatedLtv;
            } else {
                entity.setNum(updatedLtv.getNum());
            }

            if (updateAll) {
                // 更新全部字段
                for (LtvDetailField value : LtvDetailField.values()) {
                    if (days < value.getDays()) {
                        break;
                    }
                    updateLtvDetailField(entity, serverId, date, value.getDays());
                }
            }
            updateLtvDetailField(entity, serverId, date, days);

            if (entity.getId() != null) {
                updateById(entity);
            } else {
                save(entity);
            }
        }
    }

    private void updateLtvDetailField(GameStatLtvDetail entity, int serverId, String registerDate, int days) {
        int num = entity.getNum() != null ? entity.getNum() : 0;
        // 注册为0, 直接返回
        if (num <= 0) {
            return;
        }

        LtvDetailField field = LtvDetailField.valueOf(days);
        if (field != null) {
            ServerLtvAmount ltvAmount = getBaseMapper().getLtvAmount(serverId, registerDate, days);
            if (ltvAmount != null) {
                if (ltvAmount.getTotalAmount() == null) {
                    ltvAmount.setTotalAmount(BigDecimal.ZERO);
                }
                field.getFunction().accept(entity, ltvAmount.getTotalAmount());
            }
        }
    }
}
