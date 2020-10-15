package org.jeecg.modules.game.service.impl;

import cn.youai.xiuzhen.common.data.ConfigDataEnum;
import cn.youai.xiuzhen.common.data.ConfigDataService;
import cn.youai.xiuzhen.entity.pojo.ConfRechargeGoods;
import cn.youai.xiuzhen.utils.BigDecimalUtil;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.cqengine.query.logical.And;
import org.jeecg.database.DataSourceHelper;
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
import java.util.ArrayList;
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

	/**
	 * 获取数据处理
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
}
