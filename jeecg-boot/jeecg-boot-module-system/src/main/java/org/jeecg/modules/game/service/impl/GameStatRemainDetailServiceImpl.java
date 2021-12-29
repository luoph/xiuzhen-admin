/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.constant.RemainDetailField;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.game.constant.RoleType;
import org.jeecg.modules.game.entity.GameServer;
import org.jeecg.modules.game.entity.GameStatRemainDetail;
import org.jeecg.modules.game.entity.ServerRemain;
import org.jeecg.modules.game.mapper.GameStatRemainDetailMapper;
import org.jeecg.modules.game.service.IGameServerService;
import org.jeecg.modules.game.service.IGameStatRemainDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
public class GameStatRemainDetailServiceImpl extends ServiceImpl<GameStatRemainDetailMapper, GameStatRemainDetail> implements IGameStatRemainDetailService {

    @Value("${app.log.db}")
    private String logDb;

    @Autowired
    private IGameServerService gameServerService;

    /**
     * 每日留存统计详情
     */
    @Override
    public void calcRemainDetailStat(RoleType roleType, Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll) {
        String date = DateUtil.formatDate(registerDate);
        for (Integer serverId : serverIds) {
            GameServer gameServer = gameServerService.getById(serverId);
            if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
                continue;
            }

            LambdaQueryWrapper<GameStatRemainDetail> query = Wrappers.<GameStatRemainDetail>lambdaQuery()
                    .eq(GameStatRemainDetail::getServerId, serverId)
                    .eq(GameStatRemainDetail::getRoleType, roleType.getValue())
                    .eq(GameStatRemainDetail::getCountDate, registerDate);

            // 重新查询注册数量
            GameStatRemainDetail updatedEntity = null;
            if (roleType == RoleType.ALL) {
                updatedEntity = getBaseMapper().getGameStatRemainDetail(roleType.getValue(), serverId, date);
            } else if (roleType == RoleType.PAID) {
                updatedEntity = getBaseMapper().getPayGameStatRemainDetail(roleType.getValue(), serverId, date);
            }

            GameStatRemainDetail entity = getOne(QueryUtils.safeSelectOneQuery(query));
            if (entity == null) {
                entity = updatedEntity;
            }

            if (updateAll) {
                // 更新全部字段
                for (RemainDetailField value : RemainDetailField.values()) {
                    if (days + 1 < value.getDays()) {
                        break;
                    }
                    updateRemainDetailField(roleType, entity, serverId, date, value.getDays());
                }
            }
            updateRemainDetailField(roleType, entity, serverId, date, days);

            if (entity.getId() != null) {
                updateById(entity);
            } else {
                save(entity);
            }
        }
    }

    private void updateRemainDetailField(RoleType roleType, GameStatRemainDetail entity, int serverId, String registerDate, int days) {
        int baseNum = entity.getD1() != null ? entity.getD1() : 0;
        // 注册为0, 直接返回
        if (baseNum <= 0) {
            return;
        }

        RemainDetailField field = RemainDetailField.valueOf(days);
        Date tomorrow = DateUtils.endTimeOfDate(DateUtils.now());
        if (field != null && tomorrow.after(DateUtils.addDays(entity.getCountDate(), days))) {
            ServerRemain serverRemain = null;
            if (roleType == RoleType.ALL) {
                serverRemain = getBaseMapper().selectRemain(serverId, registerDate, days, logDb);
            } else if (roleType == RoleType.PAID) {
                serverRemain = getBaseMapper().selectPayRemain(serverId, registerDate, days, logDb);
            }
//            log.info("updateRemainDetailField type:{}, days:{}, registerDate:{}, serverId:{}, serverRemain:{}",
//                    roleType, days, registerDate, serverId, JSON.toJSON(serverRemain));

            if (serverRemain != null) {
                if (serverRemain.getRemain() == null) {
                    serverRemain.setRemain(0);
                }
                field.getFunction().accept(entity, serverRemain.getRemain());
            }
        }
    }

}
