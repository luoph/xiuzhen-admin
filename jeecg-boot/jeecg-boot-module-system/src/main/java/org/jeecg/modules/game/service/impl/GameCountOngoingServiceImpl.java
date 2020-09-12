/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameCountOngoing;
import org.jeecg.modules.game.entity.GameLtvCount;
import org.jeecg.modules.game.mapper.GameCountOngoingMapper;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IGameCountOngoingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 30天留存和ltv统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-09-12
 */
@Service
public class GameCountOngoingServiceImpl extends ServiceImpl<GameCountOngoingMapper, GameCountOngoing> implements IGameCountOngoingService {

    @Autowired
    private IGameChannelService gameChannelService;


    @Override
    public IPage<GameCountOngoing> selectList(Page<GameCountOngoing> page, int channelId, int serverId, int type, String rangeDateBegin, String rangeDateEnd) {
        QueryWrapper<GameCountOngoing> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("count_date");
        if (type > 0) {
            queryWrapper.eq("type", type);
        }
        boolean paramValidCheck = ParamValidUtil.isParamInValidCheck(channelId, serverId, rangeDateBegin, rangeDateEnd);
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
