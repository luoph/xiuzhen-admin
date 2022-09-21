/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.server.utils.QueryUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServer;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.constant.LtvDetailField;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.entity.GameStatLtvDetail;
import cn.youai.xiuzhen.stat.entity.ServerLtvAmount;
import cn.youai.xiuzhen.stat.mapper.GameStatLtvDetailMapper;
import cn.youai.xiuzhen.stat.service.IGameStatLtvDetailService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * LTV统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2021-12-22
 */
@Service
@DS("shardingSphere")
public class GameStatLtvDetailServiceImpl extends ServiceImpl<GameStatLtvDetailMapper, GameStatLtvDetail> implements IGameStatLtvDetailService {

    @Autowired
    private IGameServerService gameServerService;

    @Autowired
    private IGameChannelService gameChannelService;

    @Override
    public GameStatLtvDetail selectServerLtvDetail(int serverId, Date registerDate) {
        LambdaQueryWrapper<GameStatLtvDetail> query = Wrappers.<GameStatLtvDetail>lambdaQuery()
                .eq(GameStatLtvDetail::getServerId, serverId)
                .eq(GameStatLtvDetail::getChannel, StatisticType.DEFAULT_CHANNEL)
                .eq(GameStatLtvDetail::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public ServerLtvAmount queryServerLtvAmount(int serverId, Date registerDate, int days) {
        return getBaseMapper().queryServerLtvAmount(serverId, registerDate, days);
    }

    @Override
    public ServerLtvAmount queryChannelLtvAmount(String channel, Date registerDate, int days) {
        return getBaseMapper().queryChannelLtvAmount(channel, registerDate, days);
    }

    @Override
    public GameStatLtvDetail selectChannelLtvDetail(String channel, Date registerDate) {
        LambdaQueryWrapper<GameStatLtvDetail> query = Wrappers.<GameStatLtvDetail>lambdaQuery()
                .eq(GameStatLtvDetail::getServerId, 0)
                .eq(GameStatLtvDetail::getChannel, channel)
                .eq(GameStatLtvDetail::getCountDate, registerDate);
        return getOne(QueryUtils.safeSelectOneQuery(query));
    }

    @Override
    public GameStatLtvDetail queryServerLtvDetail(int serverId, Date registerDate) {
        return getBaseMapper().queryServerLtvDetail(serverId, registerDate);
    }

    @Override
    public GameStatLtvDetail queryChannelLtvDetail(String channel, Date registerDate) {
        return getBaseMapper().queryChannelLtvDetail(channel, registerDate);
    }

    @Override
    public void calcServerLtvDetail(int serverId, Date registerDate, int days, boolean updateAll) {
        GameServer gameServer = gameServerService.getById(serverId);
        if (gameServer == null || gameServer.getOpenTime().after(registerDate)) {
            return;
        }

        // 重新查询注册数量
        GameStatLtvDetail updateEntity = queryServerLtvDetail(serverId, registerDate);
        GameStatLtvDetail dbEntity = selectServerLtvDetail(serverId, registerDate);
        if (dbEntity == null) {
            dbEntity = updateEntity;
            updateEntity.setChannel(StatisticType.DEFAULT_CHANNEL);
        } else {
            dbEntity.apply(updateEntity);
        }

        if (updateAll) {
            // 更新全部字段
            for (LtvDetailField value : LtvDetailField.values()) {
                if (days < value.getDays()) {
                    break;
                }
                updateLtvDetailField(dbEntity, serverId, registerDate, value.getDays());
            }
        }

        if (LtvDetailField.valueOf(days) == null) {
            updateLtvDetailField(dbEntity, serverId, registerDate, days);
        }

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }

    @Override
    public void calcChannelLtvDetail(String channel, Date registerDate, int days, boolean updateAll) {
        GameChannel gameChannel = gameChannelService.selectChannel(channel);
        if (gameChannel == null) {
            return;
        }

        // 重新查询注册数量
        GameStatLtvDetail updateEntity = queryChannelLtvDetail(channel, registerDate);
        GameStatLtvDetail dbEntity = selectChannelLtvDetail(channel, registerDate);
        if (dbEntity == null) {
            dbEntity = updateEntity;
            updateEntity.setServerId(StatisticType.DEFAULT_SERVER_ID);
        } else {
            dbEntity.apply(updateEntity);
        }

        if (updateAll) {
            // 更新全部字段
            for (LtvDetailField value : LtvDetailField.values()) {
                if (days < value.getDays()) {
                    break;
                }
                updateLtvDetailField(dbEntity, channel, registerDate, value.getDays());
            }
        }

        if (LtvDetailField.valueOf(days) == null) {
            updateLtvDetailField(dbEntity, channel, registerDate, days);
        }

        if (dbEntity.getId() != null) {
            updateById(dbEntity);
        } else {
            save(dbEntity);
        }
    }

    private void updateLtvDetailField(GameStatLtvDetail entity, int serverId, Date registerDate, int days) {
        int num = entity.getNum() != null ? entity.getNum() : 0;
        LtvDetailField field = LtvDetailField.match(days);
        if (field == null) {
            return;
        }

        // 注册为0, 直接返回
        if (num <= 0) {
            field.getFunction().accept(entity, BigDecimal.ZERO);
        } else {
            ServerLtvAmount ltvAmount = queryServerLtvAmount(serverId, registerDate, days);
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (ltvAmount != null && ltvAmount.getTotalAmount() != null) {
                totalAmount = ltvAmount.getTotalAmount();
            }
            field.getFunction().accept(entity, totalAmount);
        }
    }

    private void updateLtvDetailField(GameStatLtvDetail entity, String channel, Date registerDate, int days) {
        int num = entity.getNum() != null ? entity.getNum() : 0;
        LtvDetailField field = LtvDetailField.match(days);
        if (field == null) {
            return;
        }

        // 注册为0, 直接返回
        if (num <= 0) {
            field.getFunction().accept(entity, BigDecimal.ZERO);
        } else {
            ServerLtvAmount ltvAmount = queryChannelLtvAmount(channel, registerDate, days);
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (ltvAmount != null && ltvAmount.getTotalAmount() != null) {
                totalAmount = ltvAmount.getTotalAmount();
            }
            field.getFunction().accept(entity, totalAmount);
        }
    }
}
