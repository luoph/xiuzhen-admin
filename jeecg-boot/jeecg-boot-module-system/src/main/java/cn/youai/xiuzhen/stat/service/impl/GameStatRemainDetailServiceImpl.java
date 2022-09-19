/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.game.constant.RoleType;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.constant.RemainDetailField;
import cn.youai.xiuzhen.stat.entity.GameStatRemainDetail;
import cn.youai.xiuzhen.stat.entity.ServerRemain;
import cn.youai.xiuzhen.stat.mapper.GameStatRemainDetailMapper;
import cn.youai.xiuzhen.stat.service.IGameStatRemainDetailService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@DS("shardingSphere")
public class GameStatRemainDetailServiceImpl extends ServiceImpl<GameStatRemainDetailMapper, GameStatRemainDetail> implements IGameStatRemainDetailService {

    @Autowired
    private IGameServerService gameServerService;

    /**
     * 每日留存统计详情
     */
    @Override
    public void calcRemainDetailStat(RoleType roleType, Collection<Integer> serverIds, Date registerDate, int days, boolean updateAll) {
        for (Integer serverId : serverIds) {
            calcRemainDetailStat(roleType, serverId, registerDate, days, updateAll);
        }
    }

    @Override
    public void calcRemainDetailStat(RoleType roleType, int serverId, Date registerDate, int days, boolean updateAll) {
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
            return;
        }

        LambdaQueryWrapper<GameStatRemainDetail> query = Wrappers.<GameStatRemainDetail>lambdaQuery()
                .eq(GameStatRemainDetail::getServerId, serverId)
                .eq(GameStatRemainDetail::getChannel, "default")
                .eq(GameStatRemainDetail::getRoleType, roleType.getValue())
                .eq(GameStatRemainDetail::getCountDate, registerDate);

        // 重新查询注册数量
        GameStatRemainDetail updatedEntity = null;
        if (roleType == RoleType.ALL) {
            updatedEntity = getBaseMapper().getStatRemainDetail(roleType.getValue(), serverId, registerDate);
        } else if (roleType == RoleType.PAID) {
            updatedEntity = getBaseMapper().getPayStatRemainDetail(roleType.getValue(), serverId, registerDate);
        } else if (roleType == RoleType.FREE) {
            updatedEntity = getBaseMapper().getFreeStatRemainDetail(roleType.getValue(), serverId, registerDate);
        }

        updatedEntity.setChannel("default");
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
                updateRemainDetailField(roleType, entity, serverId, registerDate, value.getDays());
            }
        }
        updateRemainDetailField(roleType, entity, serverId, registerDate, days);

        if (entity.getId() != null) {
            updateById(entity);
        } else {
            save(entity);
        }
    }

    private void updateRemainDetailField(RoleType roleType, GameStatRemainDetail entity, int serverId, Date registerDate, int days) {
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
                serverRemain = getBaseMapper().selectRemain(serverId, registerDate, days);
            } else if (roleType == RoleType.PAID) {
                serverRemain = getBaseMapper().selectPayRemain(serverId, registerDate, days);
            } else if (roleType == RoleType.FREE) {
                serverRemain = getBaseMapper().selectFreeRemain(serverId, registerDate, days);
            }

            log.info("updateRemainDetailField type:{}, date:{}, serverId:{}, days:{}, field:{}, remain:{}",
                    roleType, registerDate, serverId, days, field, JSON.toJSON(serverRemain));

            if (serverRemain != null) {
                if (serverRemain.getRemain() == null) {
                    serverRemain.setRemain(0);
                }
                field.getFunction().accept(entity, serverRemain.getRemain());
            }
        }
    }

}
