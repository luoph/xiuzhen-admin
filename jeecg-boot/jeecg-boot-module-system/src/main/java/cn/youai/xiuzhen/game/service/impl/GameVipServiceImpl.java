package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.entity.GameVip;
import cn.youai.xiuzhen.game.mapper.GameVipMapper;
import cn.youai.xiuzhen.game.service.IGameVipService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description VIP
 * @date 2023-01-13
 */
@Service
@DS("master")
public class GameVipServiceImpl extends ServiceImpl<GameVipMapper, GameVip> implements IGameVipService {

    @Override
    public IPage<GameVip> queryVipList(Page<?> page, GameVip entity) {
        return getBaseMapper().queryVipList(page, entity);
    }

    @Override
    public GameVip queryVip(long playerId) {
        return getOne(Wrappers.<GameVip>lambdaQuery().eq(GameVip::getPlayerId, playerId));
    }

    @Override
    public List<GameVip> queryVipList(Collection<Long> playerIds) {
        if (CollUtil.isEmpty(playerIds)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<GameVip>lambdaQuery().in(GameVip::getPlayerId, playerIds));
    }
}
