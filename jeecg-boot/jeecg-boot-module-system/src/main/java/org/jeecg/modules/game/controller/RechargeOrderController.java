package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.RechargeOrder;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IRechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 今日礼包
 * @date 2020-10-13
 */
@Slf4j
@RestController
@RequestMapping("game/rechargeOrder")
public class RechargeOrderController extends JeecgController<RechargeOrder, IRechargeOrderService> {

	/**
	 * 礼包类型
	 */
	private static final int[] GOODS_TYPE = {0, 1, 2, 3, 4,};

	@Autowired
	private IRechargeOrderService rechargeOrderService;

	@Autowired
	private IGameChannelService gameChannelService;

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "普通礼包-列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
									@RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
									@RequestParam(name = "days", defaultValue = "0") int days,
									@RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
									@RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
									) {
		Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		// 没有传入时间和天数返回空的数据
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<RechargeOrder> rechargeOrders = rechargeOrderService.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[0]);
		page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
		return Result.ok(page);
	}

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "特惠礼包-列表查询")
	@GetMapping(value = "/giftBagList")
	public Result<?> giftBagList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
	                              @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
	                              @RequestParam(name = "days", defaultValue = "0") int days,
	                              @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
								  @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
								  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
								  ) {
		Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		// 没有传入时间和天数返回空的数据
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<RechargeOrder> rechargeOrders = rechargeOrderService.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[3]);
		page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
		return Result.ok(page);
	}

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "首充-列表查询")
	@GetMapping(value = "/firstCharge")
	public Result<?> firstCharge(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
	                             @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
	                             @RequestParam(name = "days", defaultValue = "0") int days,
	                             @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
	                             @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
	                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		// 没有传入时间和天数返回空的数据
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<RechargeOrder> rechargeOrders = rechargeOrderService.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[4]);
		page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
		return Result.ok(page);
	}


	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "仙职-列表查询")
	@GetMapping(value = "/officialJob")
	public Result<?> officialJob(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
	                             @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
	                             @RequestParam(name = "days", defaultValue = "0") int days,
	                             @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
	                             @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
	                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		// 没有传入时间和天数返回空的数据
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<RechargeOrder> rechargeOrders = rechargeOrderService.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[1]);
		page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
		return Result.ok(page);
	}

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "月卡-列表查询")
	@GetMapping(value = "/monthCard")
	public Result<?> monthCard(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
	                           @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
	                           @RequestParam(name = "days", defaultValue = "0") int days,
	                           @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
	                           @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
	                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
			return Result.ok(page);
		}
		// 没有传入时间和天数返回空的数据
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && days == 0) {
			return Result.ok(page);
		}
		String channel = gameChannelService.queryChannelNameById(channelId);
		List<RechargeOrder> rechargeOrders = rechargeOrderService.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[2]);
		page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
		return Result.ok(page);
	}

	/**
	 * 导出excel
	 *
	 * @param request       请求
	 * @param rechargeOrder 实体
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, RechargeOrder rechargeOrder) {
		return super.exportXls(request, rechargeOrder, RechargeOrder.class, "今日礼包");
	}


}
