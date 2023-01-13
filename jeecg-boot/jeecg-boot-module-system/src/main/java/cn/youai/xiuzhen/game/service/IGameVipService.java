package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.entity.GameVip;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 敏感词
 * @date 2023-01-10
 */
public interface IGameVipService extends IService<GameVip> {

    IPage<GameVip> queryVipList(Page<?> page, GameVip entity);

    GameVip queryVip(long playerId);

    List<GameVip> queryVipList(Collection<Long> playerIds);

}
