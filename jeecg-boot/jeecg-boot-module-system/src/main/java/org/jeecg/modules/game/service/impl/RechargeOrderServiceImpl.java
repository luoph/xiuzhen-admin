package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfRechargeGoods;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.logical.And;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.RechargeOrder;
import org.jeecg.modules.game.mapper.RechargeOrderMapper;
import org.jeecg.modules.game.service.IRechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 今日礼包
 * @date 2020-10-13
 */
@Service
public class RechargeOrderServiceImpl extends ServiceImpl<RechargeOrderMapper, RechargeOrder> implements IRechargeOrderService {

    @Resource
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private ConfigDataService configDataService;

    @Override
    public List<RechargeOrder> queryTodayGift(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel) {
        List<RechargeOrder> list = null;
        Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
        Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            // 查询商品id和商品类型
            list = rechargeOrderMapper.queryTodayGift(rangeDateBeginTime, rangeDateEndTime);
            for (RechargeOrder rechargeOrder : list) {
                Integer goodsId = rechargeOrder.getGoodsId();
                Integer goodsType = rechargeOrder.getGoodsType();
                ConfRechargeGoods rechargeGoods = itemTree(goodsId, goodsType);
                String name = "商品名字不存在";
                if (rechargeGoods == null) {
                    name = rechargeGoods.getName();
                }
            }
        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }

        return list;
    }


    public ConfRechargeGoods itemTree(Integer goodsId, int goodsType) {
        And<ConfRechargeGoods> and = and(equal(ConfRechargeGoods.ID, goodsId), equal(ConfRechargeGoods.GOODS_TYPE, goodsType));
        return configDataService.selectOne(ConfigDataEnum.RECHARGE_GOODS, ConfRechargeGoods.class, and);
    }
}
