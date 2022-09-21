/*
 * create by mybatis-plus-generator  https://github.com/xiweile
 */
package cn.youai.xiuzhen.game.service;

import cn.youai.xiuzhen.game.constant.RechargeGoodsGroup;
import cn.youai.xiuzhen.game.entity.GameRechargeGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 游戏充值商品 服务类
 * </p>
 *
 * @author buliangliang
 * @since 2021-09-18
 */
public interface IGameRechargeGoodsService extends IService<GameRechargeGoods> {

    List<GameRechargeGoods> selectByGoodsId(Collection<Integer> goodsIds);
    List<GameRechargeGoods> selectByGoodsType(Collection<Integer> goodsType);

}
