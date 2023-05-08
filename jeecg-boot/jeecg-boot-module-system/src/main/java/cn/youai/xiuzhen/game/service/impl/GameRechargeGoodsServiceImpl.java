package cn.youai.xiuzhen.game.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.youai.server.model.ItemVO;
import cn.youai.xiuzhen.game.entity.GameRechargeGoods;
import cn.youai.xiuzhen.game.mapper.GameRechargeGoodsMapper;
import cn.youai.xiuzhen.game.model.RechargeGoods;
import cn.youai.xiuzhen.game.service.IGameRechargeGoodsService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.dreamlu.mica.core.utils.$;
import org.jeecg.JsonFileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_recharge_goods
 * @date 2021-04-16
 */
@Service
@DS("master")
public class GameRechargeGoodsServiceImpl extends ServiceImpl<GameRechargeGoodsMapper, GameRechargeGoods> implements IGameRechargeGoodsService {

    @Value("${app.folder.json:}")
    private String jsonFolder;

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

    @Override
    public void refreshConfig() {
        List<GameRechargeGoods> list = list();
        List<RechargeGoods> goodsList = new ArrayList<>();
        for (GameRechargeGoods it : list) {
            RechargeGoods copy = $.copy(it, RechargeGoods.class);
            assert copy != null;

            // 适配前端的字段 items -> rewards, addition -> additions
            if (StringUtils.isNotEmpty(it.getItems())) {
                copy.setRewards(JSON.parseArray(it.getItems(), ItemVO.class));
            }

            if (StringUtils.isNotEmpty(it.getAddition())) {
                copy.setAdditions(JSON.parseArray(it.getAddition(), ItemVO.class));
            }
            goodsList.add(copy);
        }
        JsonFileUtils.writeJsonFile(goodsList, jsonFolder, "recharge_goods");
    }
}
