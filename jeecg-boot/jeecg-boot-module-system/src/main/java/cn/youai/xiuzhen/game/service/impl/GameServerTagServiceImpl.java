package cn.youai.xiuzhen.game.service.impl;

import cn.youai.xiuzhen.game.entity.GameServerTag;
import cn.youai.xiuzhen.game.mapper.GameServerTagMapper;
import cn.youai.xiuzhen.game.service.IGameServerTagService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏服标签
 * @date 2022-03-25
 */
@Service
public class GameServerTagServiceImpl extends ServiceImpl<GameServerTagMapper, GameServerTag> implements IGameServerTagService {

    @Override
    public List<GameServerTag> selectTags() {
        Wrapper<GameServerTag> query = Wrappers.<GameServerTag>lambdaQuery()
                .select(GameServerTag::getId, GameServerTag::getName, GameServerTag::getPosition);
        return list(query);
    }

}
