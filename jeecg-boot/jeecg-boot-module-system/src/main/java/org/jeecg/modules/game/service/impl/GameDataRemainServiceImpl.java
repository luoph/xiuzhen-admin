/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameChannel;
import org.jeecg.modules.game.entity.GameStatDataRemain;
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
public class GameDataRemainServiceImpl extends ServiceImpl<GameDataRemainMapper, GameStatDataRemain> implements IGameDataRemainService {

    @Autowired
    private IGameChannelService gameChannelService;
    @Resource
    private GameDataRemainMapper gameDataRemainMapper;
    @Value("${app.log.db.table}")
    private String logTable;

    @Override
    public IPage<GameStatDataRemain> selectList(Page<GameStatDataRemain> page, int channelId, int serverId, String rangeDateBegin, String rangeDateEnd) {
        QueryWrapper<GameStatDataRemain> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("count_date");
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

    @Override
    public GameStatDataRemain getCountRemain(String channel, int serverId, String date) {
        return gameDataRemainMapper.gameRemainCount(channel, serverId, date, logTable);
    }
}
