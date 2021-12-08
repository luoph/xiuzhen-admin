package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.game.entity.ShopMallLog;
import org.jeecg.modules.game.service.IShopMallLogService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xubodong
 * @version 1.0.0
 * @date 2020/10/17 18:34
 */
@Slf4j
@RestController
@RequestMapping("game/shopMallLog")
public class ShopMallLogController {

    @Resource
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
        Page<OneDayDate> page = new Page<>(pageNo, pageSize);

        //服务器空校验
        if (0 == serverId || 0 == channelId) {
            return Result.error("请选择服务器！");
        }
        //日期校验
        DateRange dateRange = ParamValidUtil.getDateRange(rangeDateBegin, rangeDateEnd, days);
        if (null == dateRange) {
            return Result.error("请选择日期！");
        }

        if (0 == type) {
            return Result.error("请选择商店类型！");
        }

        List<ShopMallLog> shopMallLogs = shopMallLogService.queryShopMallListNew(DateUtils.formatDate(dateRange.getStart(), DatePattern.NORM_DATETIME_PATTERN), DateUtils.formatDate(dateRange.getEnd(), DatePattern.NORM_DATETIME_PATTERN), days, serverId, type);

        //重新整理数据给前端
        Map<String, List<ShopMallLog>> shopMallLogListMapTime = shopMallLogs.stream().collect(Collectors.groupingBy(ShopMallLog::getCreateTimeString));
        Map<String, List<ShopMallLog>> shopMallLogListMapTimeDesc = shopMallLogListMapTime.entrySet()
                .stream()
                .sorted(Collections
                        .reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        List<OneDayDate> shopMallLogsOneDayDate = new ArrayList<>();
        for (String s : shopMallLogListMapTimeDesc.keySet()) {
            List<ShopMallLog> shopMallLogListOneDay = shopMallLogListMapTimeDesc.get(s);
            //总货币数
            int sum = shopMallLogListOneDay.stream().mapToInt(shopMallLog1 -> shopMallLog1.getItemNum().intValue()).sum();
            for (ShopMallLog shopMallLog : shopMallLogListOneDay) {
                if (0 == shopMallLog.getItemNum().intValue() || 0 == sum) {
                    shopMallLog.setItemNumRate(new BigDecimal(0));
                    continue;
                }
                shopMallLog.setItemNumRate(shopMallLog.getItemNum().divide(new BigDecimal(sum), 2, BigDecimal.ROUND_HALF_UP));
            }
            OneDayDate oneDayDate = new OneDayDate();
            oneDayDate.setTime(s);
            oneDayDate.setShopMallLogList(shopMallLogListMapTimeDesc.get(s));
            shopMallLogsOneDayDate.add(oneDayDate);
        }

        page.setRecords(shopMallLogsOneDayDate).setTotal(shopMallLogsOneDayDate.size());
        return Result.ok(page);
    }

    /**
     * 返回给前端的某一天的数据对象
     */
    @Data
    private static class OneDayDate {
        String time;
        List<ShopMallLog> shopMallLogList;
    }


}
