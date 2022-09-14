/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.stat.service.impl;

import cn.youai.xiuzhen.stat.entity.GameStatRemain;
import cn.youai.xiuzhen.stat.mapper.GameDataRemainMapper;
import cn.youai.xiuzhen.stat.service.IGameDataRemainService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 留存统计 服务实现类
 * </p>
 *
 * @author buliangliang
 * @since 2020-08-26
 */
@Service
@DS("shardingSphere")
public class GameDataRemainServiceImpl extends ServiceImpl<GameDataRemainMapper, GameStatRemain> implements IGameDataRemainService {

    @Resource
    private GameDataRemainMapper gameDataRemainMapper;

    @Override
    public IPage<GameStatRemain> selectList(Page<GameStatRemain> page, int serverId, String rangeDateBegin, String rangeDateEnd) {
        LambdaQueryWrapper<GameStatRemain> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.between(GameStatRemain::getCountDate, rangeDateBegin, rangeDateEnd);
        queryWrapper.eq(GameStatRemain::getServerId, serverId);
        queryWrapper.orderByDesc(GameStatRemain::getCountDate);
        return page(page, queryWrapper);
    }

    @Override
    public GameStatRemain getCountRemain(int serverId, String date, Date statDate) {
        return gameDataRemainMapper.gameRemainCount(serverId, date, statDate);
    }
}
