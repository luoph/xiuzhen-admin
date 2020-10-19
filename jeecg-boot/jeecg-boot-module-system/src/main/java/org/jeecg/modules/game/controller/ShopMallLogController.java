package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.game.entity.ShopMallLog;
import org.jeecg.modules.game.service.IShopMallLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/17 18:34
 */
@Slf4j
@RestController
@RequestMapping("game/shopMallLog")
public class ShopMallLogController {

	@Autowired
	private IShopMallLogService shopMallLogService;

	/**
	 * 分页列表查询
	 *
	 * @param pageNo   页码
	 * @param pageSize 分页大小
	 * @return {@linkplain Result}
	 */
	@AutoLog(value = "商店销售-列表查询")
	@GetMapping(value = "/list")
	public Result<?> currencyPayIncomeList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
	                                       @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
	                                       @RequestParam(name = "days", defaultValue = "0") int days,
	                                       @RequestParam(name = "type", defaultValue = "0") int type,
	                                       @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
	                                       @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
	                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
	                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
	) {
		Page<ShopMallLog> page = new Page<>(pageNo, pageSize);
		if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0 && type == 0) {
			return Result.ok(page);
		}
		// 如果选择开始时间和结束时间是同一天
		if (rangeDateBegin.equals(rangeDateEnd)){
			rangeDateBegin = rangeDateBegin + " 00:00:00";
			rangeDateEnd = rangeDateEnd + " 23:59:59";
		}
		List<ShopMallLog> shopMallLogs = shopMallLogService.queryShopMallList(rangeDateBegin, rangeDateEnd, days, serverId, type);
		page.setRecords(shopMallLogs).setTotal(shopMallLogs.size());
		return Result.ok(page);
	}

}
