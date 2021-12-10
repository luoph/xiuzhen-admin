/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package org.jeecg.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.game.entity.GameStatLtv;
import org.jeecg.modules.game.mapper.GameStatLtvMapper;
import org.jeecg.modules.game.service.IGameStatLtvService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * LTV统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Service
public class GameStatLtvServiceImpl extends ServiceImpl<GameStatLtvMapper, GameStatLtv> implements IGameStatLtvService {

    @Resource
    private GameStatLtvMapper gameLtvCountMapper;

    @Value("${app.log.db.table}")
    private String logTable;

    @Override
    public IPage<GameStatLtv> selectList(Page<GameStatLtv> page, int serverId, String rangeDateBegin, String rangeDateEnd) {
        QueryWrapper<GameStatLtv> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("count_date");
        queryWrapper.ge("count_date", rangeDateBegin);
        queryWrapper.le("count_date", rangeDateEnd);
        queryWrapper.eq("server_id", serverId);
        return page(page, queryWrapper);
    }

    @Override
    public GameStatLtv getGameStatLtv(int serverId, Date registerDate) {
        return gameLtvCountMapper.getGameStatLtv(serverId, registerDate);
    }
}
