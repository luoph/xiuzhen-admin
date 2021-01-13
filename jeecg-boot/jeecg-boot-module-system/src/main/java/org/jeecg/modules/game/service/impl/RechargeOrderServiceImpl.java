package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfRechargeGoods;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.logical.And;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.GameChalcedonyOrder;
import org.jeecg.modules.game.entity.PayOrderBill;
import org.jeecg.modules.game.entity.RechargeOrder;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.mapper.RechargeOrderMapper;
import org.jeecg.modules.game.service.IRechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static org.jeecg.modules.game.constant.FairyJadeBuyType.*;

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

    @Resource
    private PayOrderBillMapper payOrderBillMapper;

    @Autowired
    private ConfigDataService configDataService;

    @Value("${app.log.db.table}")
    private String logTable;

    @Override
    public List<RechargeOrder> queryGiftList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel, int goodsType) {
        // 判断是否输入查询天数
        if (days == 0) {
            Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
            Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
            return getDataTreating(DateUtils.dateOnly(rangeDateBeginTime), DateUtils.dateOnly(rangeDateEndTime), serverId, channel, goodsType);
        } else {
            Date nowDate = new Date();
            Date pastDate = DateUtils.addDays(nowDate, days * (-1));
            return getDataTreating(DateUtils.dateOnly(pastDate), DateUtils.dateOnly(nowDate), serverId, channel, goodsType);
        }
    }

    @Override
    public List<GameChalcedonyOrder> queryExpendGiftList(String rangeDateBegin, String rangeDateEnd, int days, Integer serverId, String channel, String goodsType) {
        // 判断是否输入查询天数
        if (days == 0) {
            return getExpendDataTreatingC(rangeDateBegin, rangeDateEnd, serverId, channel, goodsType);
        } else {
            Date nowDate = new Date();
            Date pastDate = DateUtils.addDays(nowDate, days * (-1));
            return getExpendDataTreatingC(new SimpleDateFormat("yyyy-MM-dd").format(pastDate), new SimpleDateFormat("yyyy-MM-dd").format(nowDate), serverId, channel, goodsType);
        }
    }

    /**
     * 获取充值礼包数据处理
     */
    private List<RechargeOrder> getDataTreating(Date rangeDateBeginTime, Date rangeDateEndTime, Integer serverId, String channel, int goodsType) {
        // 封装商品id和商品类型
        List<RechargeOrder> list = new ArrayList<>();
        // 封装数据填充模板
        List<RechargeOrder> rechargeOrderList = new ArrayList<>();
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            // 查询商品id和商品类型 (普通类型的礼包)
            list = rechargeOrderMapper.queryGiftList(rangeDateBeginTime, rangeDateEndTime, goodsType);

        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }

        for (RechargeOrder rechargeOrder : list) {
            Integer goodsId = rechargeOrder.getGoodsId();
            // 在默认数据源下查询 dau 和 礼包信息
            DataSourceHelper.useDefaultDatabase();
            // 通过登录日志统计当前时间段的dau
            Long dau = rechargeOrderMapper.queryDAU(rangeDateBeginTime, rangeDateEndTime, serverId, channel, logTable);
            // 通过商品id和dau 获取封装数据的统计对象
            PayOrderBill payOrderBill = payOrderBillMapper.queryPayOrderList(rangeDateBeginTime, rangeDateEndTime, serverId, channel, dau, goodsId);

            // 获取充值商品
            ConfRechargeGoods rechargeGoods = itemTree(goodsId, goodsType);

            if (rechargeGoods == null) {
                // 可能存在数据不一致的情况
                rechargeGoods.setName("该商品不存在");
            }
            // 填充数据对象返回给前端
            rechargeOrder.setGoodsName(rechargeGoods.getName());
            rechargeOrder.setGoodsId(goodsId);
            rechargeOrder.setDau(dau);
            rechargeOrder.setPayNum(payOrderBill.getPayNumSum());
            rechargeOrder.setPayNumRate(BigDecimalUtil.dividePercent(payOrderBill.getPayNumSumRate().doubleValue()));
            rechargeOrder.setGoodsPrice(payOrderBill.getOrderAmount());
            rechargeOrder.setPayAccountSum(payOrderBill.getPayAmountSum());
            rechargeOrder.setRangeTimeDate(rangeDateBeginTime + "~" + rangeDateEndTime);
            rechargeOrder.setConsumeRank(payOrderBill.getConsumeRank());
            rechargeOrder.setPayCount(payOrderBill.getPayCount());
            rechargeOrder.setChalcedony(payOrderBill.getChalcedony());

            // 封装为list集合
            rechargeOrderList.add(rechargeOrder);
        }

        return rechargeOrderList;
    }


    /**
     * 通过商品id和商品类型获取充值商品(策划表中的数据)
     *
     * @param goodsId
     * @param goodsType
     * @return
     */
    private ConfRechargeGoods itemTree(Integer goodsId, int goodsType) {
        And<ConfRechargeGoods> and = and(equal(ConfRechargeGoods.ID, goodsId), equal(ConfRechargeGoods.GOODS_TYPE, goodsType));
        return configDataService.selectOne(ConfigDataEnum.RECHARGE_GOODS, ConfRechargeGoods.class, and);
    }


    /**
     * 获取消耗礼包数据处理
     *
     * @param rangeDateBeginTime
     * @param rangeDateEndTime
     * @param serverId
     * @param channel
     * @return
     */
    private List<GameChalcedonyOrder> getExpendDataTreatingC(String rangeDateBeginTime, String rangeDateEndTime, Integer serverId, String channel, String goodsType) {
        List<GameChalcedonyOrder> gameChalcedonyOrderList = new ArrayList<>();
        List<Map> fairyJadeBuyInfoList = null;
        try {
            // 通过serverId切换数据源
            DataSourceHelper.useServerDatabase(serverId);
            //特惠礼包
            if (goodsType.equals(DAILY_GIFT.getType().toString())) {
                //查询时间范围内消耗明细
                fairyJadeBuyInfoList = rechargeOrderMapper.queryFairyJadeBuyInfo(rangeDateBeginTime, rangeDateEndTime, DAILY_GIFT.getType());
                //冲榜礼包（开服礼包）
            } else if (goodsType.equals(OPEN_SERVICE_GIFT.getType().toString())){
                //查询时间范围内消耗明细
                fairyJadeBuyInfoList = rechargeOrderMapper.queryFairyJadeBuyInfo(rangeDateBeginTime, rangeDateEndTime, OPEN_SERVICE_GIFT.getType());
                //0元购
            } else if (goodsType.equals(ZERO_BUY.getType().toString())) {
                //查询时间范围内消耗明细
                fairyJadeBuyInfoList = rechargeOrderMapper.queryFairyJadeBuyInfo(rangeDateBeginTime, rangeDateEndTime, ZERO_BUY.getType());
            } else {
                log.error("不存在的商品类型:" + goodsType);
            }

        } catch (Exception e) {
            log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
        } finally {
            DataSourceHelper.useDefaultDatabase();
        }
        //重组消耗明细信息
        List<Map> fairyJadeBuyInfoNewList = new ArrayList<>();
        for (Map map : fairyJadeBuyInfoList) {
            JSONArray dangweiPriceJsonArray = new JSONArray();
            if (!("[null]").equals(map.get("price").toString()) && !("null").equals(map.get("price").toString()) ) {
                dangweiPriceJsonArray = JSONArray.parseArray(map.get("price").toString());
                for (int i = 0; i < dangweiPriceJsonArray.size(); i++) {
                    Map<String, String> fairyJadeBuyInfoNewMap = new HashMap<>();
                    fairyJadeBuyInfoNewMap.put("player_id", map.get("player_id").toString());
                    fairyJadeBuyInfoNewMap.put("type", map.get("type").toString());
                    fairyJadeBuyInfoNewMap.put("config_id", map.get("config_id").toString());
                    fairyJadeBuyInfoNewMap.put("num", map.get("num").toString());
                    JSONObject jsonObject = dangweiPriceJsonArray.getJSONObject(i);
                    fairyJadeBuyInfoNewMap.put("itemId", jsonObject.getString("itemId"));
                    fairyJadeBuyInfoNewMap.put("itemNum", jsonObject.getString("num"));
                    fairyJadeBuyInfoNewMap.put("create_time", map.get("create_time").toString());
                    fairyJadeBuyInfoNewList.add(fairyJadeBuyInfoNewMap);
                }
            }else{
                Map<String, String> fairyJadeBuyInfoNewMap = new HashMap<>();
                fairyJadeBuyInfoNewMap.put("player_id", map.get("player_id").toString());
                fairyJadeBuyInfoNewMap.put("type", map.get("type").toString());
                fairyJadeBuyInfoNewMap.put("config_id", map.get("config_id").toString());
                fairyJadeBuyInfoNewMap.put("num", map.get("num").toString());
                fairyJadeBuyInfoNewMap.put("itemId", "免费");
                fairyJadeBuyInfoNewMap.put("itemNum", "0");
                fairyJadeBuyInfoNewMap.put("create_time", map.get("create_time").toString());
                fairyJadeBuyInfoNewList.add(fairyJadeBuyInfoNewMap);
            }

        }
//        //过滤出玉髓明细
//        List<Map> fairyList = fairyJadeBuyInfoNewList.stream().filter(map -> map.get("itemId").toString().equals("1010")).collect(Collectors.toList());
        //玉髓明细 以日期分组
        Map<String, List<Map>> fairyJadeBuyInfoMaoTime = fairyJadeBuyInfoNewList.stream().collect(Collectors.groupingBy(map -> map.get("create_time").toString().substring(0, 10)));
        //玉髓明细 排序 倒叙
        Map<String, List<Map>> fairyJadeBuyInfoMapTimeSort = fairyJadeBuyInfoMaoTime.entrySet()
                .stream()
                .sorted(Collections
                        .reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));
        //遍历获取日期内的消耗玉髓明细数据
        for (String s : fairyJadeBuyInfoMapTimeSort.keySet()) {
            // 通过登录日志统计当前时间段的dau
            Long dau = rechargeOrderMapper.queryDAU(DateUtils.dateOnly(DateUtils.parseDate(s)), DateUtils.dateOnly(DateUtils.parseDate(s)), serverId, channel, logTable);
            List<Map> oneDayFairyJadeBuyInfoList = fairyJadeBuyInfoMapTimeSort.get(s);
            //以档位分组
            Map<String, List<Map>> oneDayFairyJadeBuyInfoListItemNum = oneDayFairyJadeBuyInfoList.stream().collect(Collectors.groupingBy(map -> map.get("itemNum").toString()));
            //遍历档位
            for (String s1 : oneDayFairyJadeBuyInfoListItemNum.keySet()) {
                GameChalcedonyOrder gameChalcedonyOrder = new GameChalcedonyOrder();
                gameChalcedonyOrder.setConsumeRank(new BigDecimal(s1));
                //这个档位下用户购买信息
                List<Map> gradeBuyInfo = oneDayFairyJadeBuyInfoListItemNum.get(s1);
                //以player_id分组
                Map<String, List<Map>> oneDayFairyJadeBuyInfoListPlayId = gradeBuyInfo.stream().collect(Collectors.groupingBy(map -> map.get("player_id").toString()));
                gameChalcedonyOrder.setPayNum(oneDayFairyJadeBuyInfoListPlayId.size());
                gameChalcedonyOrder.setPayCount(gradeBuyInfo.stream().mapToInt(Map -> Integer.parseInt(Map.get("num").toString())).sum());
                gameChalcedonyOrder.setDau(dau);
                //计算并设置购买率
                BigDecimal payCountCount = new BigDecimal(oneDayFairyJadeBuyInfoListPlayId.size());
                BigDecimal dauCount = new BigDecimal(dau);
                BigDecimal divide = payCountCount.divide(dauCount, 2, BigDecimal.ROUND_HALF_UP);
                gameChalcedonyOrder.setPayNumRate(BigDecimalUtil.dividePercent(divide.doubleValue()).toString());
                gameChalcedonyOrder.setRangeTimeDate(s);
                gameChalcedonyOrder.setChalcedony(new BigDecimal(Integer.parseInt(s1) * gameChalcedonyOrder.getPayCount()));
                gameChalcedonyOrderList.add(gameChalcedonyOrder);
            }
        }
        return gameChalcedonyOrderList;
    }

}
