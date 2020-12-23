package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfRechargeGoods;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.logical.And;
import org.jeecg.database.DataSourceHelper;
import org.jeecg.modules.game.entity.GameChalcedonyOrder;
import org.jeecg.modules.game.entity.PayOrderBill;
import org.jeecg.modules.game.entity.RechargeOrder;
import org.jeecg.modules.game.mapper.PayOrderBillMapper;
import org.jeecg.modules.game.mapper.PayOrderGiftMapper;
import org.jeecg.modules.game.mapper.RechargeOrderMapper;
import org.jeecg.modules.game.service.IRechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
			Date rangeDateBeginTime = DateUtils.parseDate(rangeDateBegin);
			Date rangeDateEndTime = DateUtils.parseDate(rangeDateEnd);
			return getExpendDataTreating(rangeDateBegin, rangeDateEnd, serverId, channel, goodsType);
		} else {
			Date nowDate = new Date();
			Date pastDate = DateUtils.addDays(nowDate, days * (-1));
			return getExpendDataTreating(new SimpleDateFormat("yyyy-MM-dd").format(pastDate), new SimpleDateFormat("yyyy-MM-dd").format(nowDate), serverId, channel, goodsType);
		}
	}

	/**
	 * 获取充值礼包数据处理
	 *
	 * @param rangeDateBeginTime
	 * @param rangeDateEndTime
	 * @param serverId
	 * @param channel
	 * @return
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
	private List<GameChalcedonyOrder> getExpendDataTreating(String rangeDateBeginTime, String rangeDateEndTime, Integer serverId, String channel, String goodsType) {
		// 数据库中查询到的商品消耗记录
		List<Map> list = new ArrayList<>();
		// 封装数据填充模板
		List<GameChalcedonyOrder> gameChalcedonyOrderList = new ArrayList<>();
		String  tableDateColumn= "";
		String buyTimes = "";
		try {
			// 通过serverId切换数据源
			DataSourceHelper.useServerDatabase(serverId);
			if(goodsType.equals("特惠礼包")){
				// 查询特惠礼包商品的玉髓兑换情况
				list = rechargeOrderMapper.queryPreferenceGiftList(rangeDateBeginTime, rangeDateEndTime);
				tableDateColumn = "buy_date";
				buyTimes = "buy_times";
			}else if(goodsType.equals("冲榜礼包")){
				list = rechargeOrderMapper.queryAtListGiftList(rangeDateBeginTime, rangeDateEndTime);
				tableDateColumn = "create_time";
				buyTimes = "";
			}else if(goodsType.equals("0元购")){
				list = rechargeOrderMapper.queryZeroBuyGiftList(rangeDateBeginTime, rangeDateEndTime);
				tableDateColumn = "create_time";
				buyTimes = "";
			}else{
				log.error("不存在的商品类型:" + goodsType);
			}

		} catch (Exception e) {
			log.error("通过服务器id:" + serverId + ",切换数据源异常", e);
		} finally {
			DataSourceHelper.useDefaultDatabase();
		}

		// 在默认数据源下查询 dau 和 礼包信息
		DataSourceHelper.useDefaultDatabase();

		String buyDate = "";
		int payCount = 0;
		int payNum = 0;
		for (int i = 0; i < list.size(); i++) {
				if(!buyDate.equals(list.get(i).get(tableDateColumn).toString())){
					// 通过登录日志统计当前时间段的dau
					Long dau = rechargeOrderMapper.queryDAU(DateUtils.dateOnly(DateUtils.parseDate(buyDate)), DateUtils.dateOnly(DateUtils.parseDate(buyDate)), serverId, channel, logTable);
					GameChalcedonyOrder gameChalcedonyOrder = new GameChalcedonyOrder();
					gameChalcedonyOrder.setPayCount(payCount);
					BigDecimal payCountCount = new BigDecimal(payCount);
					BigDecimal dauCount = new BigDecimal(dau);
					if(dau==0){
						gameChalcedonyOrder.setPayNumRate("0%");
					}else{
						//计算并设置购买率
						BigDecimal divide = payCountCount.divide(dauCount,2, BigDecimal.ROUND_HALF_UP);
						gameChalcedonyOrder.setPayNumRate(BigDecimalUtil.dividePercent(divide.doubleValue()).toString());
					}
					gameChalcedonyOrder.setPayNum(payNum);
					gameChalcedonyOrder.setRangeTimeDate(buyDate);
					gameChalcedonyOrder.setId(i+1);
					gameChalcedonyOrder.setDau(dau);
//			gameChalcedonyOrder.setConsumeRank();
//			gameChalcedonyOrder.setChalcedony();
					//提交当前日期下统计的数据
					if(null != buyDate && !buyDate.equals("")){
						gameChalcedonyOrderList.add(gameChalcedonyOrder);
					}
					//重置统计的日期
					buyDate = list.get(i).get(tableDateColumn).toString();
					//重置购买次数
					payCount = buyTimes.equals("") ? 1 : Integer.parseInt(list.get(i).get(buyTimes).toString());
					//重置购买人数
					payNum = 1;
				}else{
					//叠加购买次数和购买人数
					payCount += buyTimes.equals("") ? 1 : Integer.parseInt(list.get(i).get(buyTimes).toString());
					payNum += 1;
				}

		}

		return gameChalcedonyOrderList;
	}
}
