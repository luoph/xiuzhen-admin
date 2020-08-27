/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameLtvCount;
import org.jeecg.modules.game.mapper.GameLtvCountMapper;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameDataCountService;
import org.jeecg.modules.game.service.IGameLtvCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * LTV统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Service
public class GameLtvCountServiceImpl extends ServiceImpl<GameLtvCountMapper, GameLtvCount> implements IGameLtvCountService {

    @Autowired
    private IGameChannelService gameChannelService;
    @Autowired
    private IGameDataCountService gameDataCountService;
    @Resource
    private GameLtvCountMapper gameLtvCountMapper;
    @Value("${app.log.db.table}")
    private String logTable;

    @Override
    public IPage<GameLtvCount> selectList(Page<GameLtvCount> page, int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        QueryWrapper<GameLtvCount> queryWrapper = new QueryWrapper<>();
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

    @Override
    public GameLtvCount getGameLtvCount(String channel, int serverId, String date, String logTable) {
        return gameLtvCountMapper.getGameLtvCount(channel, serverId, date, logTable);
    }
}
