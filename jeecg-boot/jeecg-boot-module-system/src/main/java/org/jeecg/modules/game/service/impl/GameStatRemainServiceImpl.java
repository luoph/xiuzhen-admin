/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameStatRemain;
import org.jeecg.modules.game.mapper.GameStatRemainMapper;
import org.jeecg.modules.game.service.IGameStatRemainService;
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
public class GameStatRemainServiceImpl extends ServiceImpl<GameStatRemainMapper, GameStatRemain> implements IGameStatRemainService {

    @Resource
    private GameStatRemainMapper gameLtvCountMapper;

    @Override
    public IPage<GameStatRemain> selectList(Page<GameStatRemain> page, int serverId, String rangeDateBegin, String rangeDateEnd) {
        QueryWrapper<GameStatRemain> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("count_date");
        queryWrapper.ge("count_date", rangeDateBegin);
        queryWrapper.le("count_date", rangeDateEnd);
        queryWrapper.eq("server_id", serverId);
        return page(page, queryWrapper);
    }

    @Override
    public GameStatRemain getGameStatRemain(int serverId, String registerDate) {
        return gameLtvCountMapper.getGameStatRemain(serverId, registerDate);
    }
}
