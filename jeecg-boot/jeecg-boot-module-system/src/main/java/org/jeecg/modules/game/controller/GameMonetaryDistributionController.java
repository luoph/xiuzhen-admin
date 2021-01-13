package org.jeecg.modules.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.xiuzhen.utils.DateUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.game.entity.MonetaryDisTributionVO;
import org.jeecg.modules.game.service.IGameMonetaryDisTributionService;
import org.jeecg.modules.game.util.ParamValidUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huli
 * @Description: GameMonetaryDistributionController
 * @date 2020/12/29 10:18
 */

@RestController
@RequestMapping("game/monetaryDistribution")
public class GameMonetaryDistributionController {
    @Resource
    private IGameMonetaryDisTributionService gameMonetaryDistributionService;
    /**
     * 查询货币分布列表
     */
    @RequestMapping("/list")
    public Result<?> monetaryDistributionList(@RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                              @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                              String rangeDateBegin,
                                              String rangeDateEnd,
                                              @RequestParam(name = "days", defaultValue = "0") Integer days,
                                              @RequestParam(name = "productAndMarketType", defaultValue = "0") Integer productAndMarketType,
                                              @RequestParam(name = "quantityType", defaultValue = "0") Integer quantityType,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        //服务器空校验
        if (0 == serverId || 0 == channelId) {
            return Result.error("请选择服务器！");
        }
        //日期空校验
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (0 == days) {
                return Result.error("请选择日期！");
            } else {
                rangeDateEnd = DateUtils.formatDate(new Date(), DatePattern.NORM_DATE_PATTERN);
                rangeDateBegin= DateUtils.formatDate(DateUtils.addDays(new Date(), days * (-1) + 1), DatePattern.NORM_DATE_PATTERN);
            }
        }
        if (0 == productAndMarketType || 0 == quantityType) {
            return Result.error("产销类型和货币类型不能为空");
        }
        Page<OneDayDate> pageVo = new Page<>(pageNo, pageSize);

        List<OneDayDate> lists= new ArrayList<>();
        int dateRangeBetween = ParamValidUtil.dateRangeBetween(DateUtils.parseDate(rangeDateBegin), DateUtils.parseDate(rangeDateEnd));
        for (int i = 0; i <= dateRangeBetween; i++) {
            Date begin = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            Date end = DateUtils.addDays(DateUtils.parseDate(rangeDateBegin), i);
            String rangeDateBegin1 = DateUtils.formatDate(begin, DatePattern.NORM_DATE_PATTERN) + " 00:00:00";
            String rangeDateEnd1 = DateUtils.formatDate(end, DatePattern.NORM_DATE_PATTERN)+ " 23:59:59";
            //查询
            List<MonetaryDisTributionVO> list = gameMonetaryDistributionService.monetaryDistributionList(channelId, serverId, rangeDateBegin1, rangeDateEnd1, productAndMarketType, quantityType);
            if (list.size() == 0) {continue;}
            OneDayDate oneDayDate = new OneDayDate();
            oneDayDate.setTime(DateUtils.formatDate(begin, DatePattern.NORM_DATE_PATTERN));
            oneDayDate.setMonetaryDistributionVOs(list);
            lists.add(oneDayDate);

        }
        List<OneDayDate> sortList = lists.stream().sorted((s1, s2) -> s2.getTime().compareTo(s1.getTime())).collect(Collectors.toList());
        pageVo.setRecords(sortList).setTotal(sortList.size());
        return Result.ok(pageVo);
    }

    /**
     * 返回给前端的某一天的数据对象
     */
    @Data
    private static class OneDayDate {
        String time;
        List<MonetaryDisTributionVO> monetaryDistributionVOs;
    }

}