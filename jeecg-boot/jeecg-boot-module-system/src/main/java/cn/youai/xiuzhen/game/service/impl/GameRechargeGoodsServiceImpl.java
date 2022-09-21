package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.xiuzhen.game.constant.RechargeGoodsGroup;
import cn.youai.xiuzhen.game.entity.GameRechargeGoods;
import cn.youai.xiuzhen.game.mapper.GameRechargeGoodsMapper;
import cn.youai.xiuzhen.game.service.IGameRechargeGoodsService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_recharge_goods
 * @date 2021-04-16
 */
@Service
@DS("master")
public class GameRechargeGoodsServiceImpl extends ServiceImpl<GameRechargeGoodsMapper, GameRechargeGoods> implements IGameRechargeGoodsService {

    @Override
    public List<GameRechargeGoods> selectByGoodsId(Collection<Integer> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<GameRechargeGoods>lambdaQuery().in(GameRechargeGoods::getGoodsId, goodsIds));
    }

    @Override
    public List<GameRechargeGoods> selectByGoodsType(Collection<Integer> goodsType) {
        if (CollUtil.isEmpty(goodsType)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<GameRechargeGoods>lambdaQuery().in(GameRechargeGoods::getGoodsType, goodsType));
    }
}
