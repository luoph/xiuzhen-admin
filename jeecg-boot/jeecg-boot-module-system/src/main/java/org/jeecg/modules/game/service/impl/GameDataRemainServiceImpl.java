/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameStatRemain;
import org.jeecg.modules.game.mapper.GameDataRemainMapper;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameDataRemainService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 留存统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Service
public class GameDataRemainServiceImpl extends ServiceImpl<GameDataRemainMapper, GameStatRemain> implements IGameDataRemainService {

    @Autowired
    private IGameChannelService gameChannelService;
    @Resource
    private GameDataRemainMapper gameDataRemainMapper;
    @Value("${app.log.db.table}")
    private String logTable;

    @Override
    public IPage<GameStatRemain> selectList(Page<GameStatRemain> page, int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        LambdaQueryWrapper<GameStatRemain> queryWrapper = Wrappers.<GameStatRemain>lambdaQuery();
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck) {
            queryWrapper.between(GameStatRemain::getCountDate, rangeDateBegin, rangeDateEnd);
            GameChannel gameChannel = gameChannelService.getById(channelId);
            if (gameChannel != null) {
                queryWrapper.eq(GameStatRemain::getChannel, gameChannel.getSimpleName()).eq(GameStatRemain::getServerId, serverId);
            }
        }
        queryWrapper.orderByDesc(GameStatRemain::getCountDate);
        return page(page, queryWrapper);
    }

    @Override
    public GameStatRemain getCountRemain(String channel, int serverId, String date) {
        return gameDataRemainMapper.gameRemainCount(channel, serverId, date, logTable);
    }
}
