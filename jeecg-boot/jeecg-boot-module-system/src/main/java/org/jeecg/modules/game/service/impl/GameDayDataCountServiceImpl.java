/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameDayDataCount;
import org.jeecg.modules.game.mapper.GameDayDataCountMapper;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameDataCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.service.IGameDayDataCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 每日数据统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-22
 */
@Service
public class GameDayDataCountServiceImpl extends ServiceImpl<GameDayDataCountMapper, GameDayDataCount> implements IGameDayDataCountService {

    @Autowired
    private IGameChannelService gameChannelService;
    @Autowired
    private IGameDataCountService gameDataCountService;

    @Override
    public IPage<GameDayDataCount> selectList(Page<GameDayDataCount> page, int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        QueryWrapper<GameDayDataCount> queryWrapper = new QueryWrapper<>();
        boolean paramValidCheck = gameDataCountService.isParamValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
        if (!paramValidCheck) {
            GameChannel gameChannel = gameChannelService.getById(channelId);
            queryWrapper.ge("count_date", rangeDateBegin);
            queryWrapper.le("count_date", rangeDateEnd);
            if (gameChannel != null) {
                queryWrapper.eq("channel", gameChannel.getSimpleName());
                queryWrapper.eq("server_id", serverId);
            }
        }
        return page(page, queryWrapper);
    }
}
