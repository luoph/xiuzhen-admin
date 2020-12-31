package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.game.entity.ShopMallLog;
import org.jeecg.modules.game.service.IShopMallLogService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                           @RequestParam(name = "type", defaultValue = "-1") int type,
                                           @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                           @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<Mo> page = new Page<>(pageNo, pageSize);
        if (!StringUtils.isEmpty(rangeDateBegin) && !StringUtils.isEmpty(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        //服务器空校验
        if(0 == serverId || 0 == channelId){
            return Result.error("请选择服务器！");
        }
        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if(0 == days){
                return Result.error("请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN) + " 23:59:59";;
                rangeDateBegin = DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN) + " 00:00:00";;
            }
        }
        //服务器空校验
        if(-1 == type || -1 == type){
            return Result.error("请选择商店类型！");
        }

        List<ShopMallLog> shopMallLogs = shopMallLogService.queryShopMallListNew(rangeDateBegin, rangeDateEnd, days, serverId, type);

        //重新整理数据给前端
        Map<String, List<ShopMallLog>> shopMallLogListMap_Time= shopMallLogs.stream().collect(Collectors.groupingBy(ShopMallLog::getCreateTimeString));
        Map<String,List<ShopMallLog>> shopMallLogListMap_Time_desc= shopMallLogListMap_Time.entrySet()
                .stream()
                .sorted(Collections
                        .reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2)->e1, LinkedHashMap::new));

        List<Mo> shopMallLogsMo = new ArrayList<>();
        for (String s : shopMallLogListMap_Time_desc.keySet()) {
            List<ShopMallLog> shopMallLogListOneDay = shopMallLogListMap_Time_desc.get(s);
            //总货币数
            Integer sum = shopMallLogListOneDay.stream().mapToInt(shopMallLog1 -> shopMallLog1.getItemNum().intValue()).sum();
            for (ShopMallLog shopMallLog : shopMallLogListOneDay) {
                if(0 == shopMallLog.getItemNum().intValue() || 0 == sum){
                    shopMallLog.setItemNumRate(new BigDecimal(0));
                    continue;
                }
                shopMallLog.setItemNumRate(shopMallLog.getItemNum().divide(new BigDecimal(sum),2,BigDecimal.ROUND_HALF_UP));
            }
            Mo mo = new Mo();
            mo.setTime(s);
            mo.setShopMallLogList(shopMallLogListMap_Time_desc.get(s));
            shopMallLogsMo.add(mo);
        }

        page.setRecords(shopMallLogsMo).setTotal(shopMallLogsMo.size());
        return Result.ok(page);
    }

    private class Mo {
        String time;
        List<ShopMallLog> shopMallLogList;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ShopMallLog> getShopMallLogList() {
            return shopMallLogList;
        }

        public void setShopMallLogList(List<ShopMallLog> shopMallLogList) {
            this.shopMallLogList = shopMallLogList;
        }
    }

}
