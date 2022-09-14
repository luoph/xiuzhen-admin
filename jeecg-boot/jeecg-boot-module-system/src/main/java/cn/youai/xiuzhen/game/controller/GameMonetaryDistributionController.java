package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.date.DatePattern;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.MonetaryDisTributionVO;
import cn.youai.xiuzhen.game.service.IGameMonetaryDisTributionService;
import cn.youai.xiuzhen.utils.ParamUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

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
        // 服务器空校验
        if (0 == serverId || 0 == channelId) {
            return Result.error("请选择服务器！");
        }

        if (0 == productAndMarketType || 0 == quantityType) {
            return Result.error("产销类型和货币类型不能为空");
        }

        // 日期空校验
        DateRange dateRange = ParamUtils.getDateRange(rangeDateBegin, rangeDateEnd, days);
        if (null == dateRange) {
            return Result.error("请选择日期！");
        }

        Page<OneDayDate> page = new Page<>(pageNo, pageSize);
        Map<Date, List<MonetaryDisTributionVO>> map = gameMonetaryDistributionService.monetaryDistributionList(channelId, serverId, dateRange, productAndMarketType, quantityType);
        List<OneDayDate> lists = new ArrayList<>();
        map.forEach((k, v) -> lists.add(new OneDayDate(DateUtils.formatDate(k, DatePattern.NORM_DATE_PATTERN), v)));
        lists.sort(Comparator.comparing(OneDayDate::getTime));
        page.setRecords(lists).setTotal(lists.size());
        return Result.ok(page);
    }

    /**
     * 返回给前端的某一天的数据对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OneDayDate {
        private String time;
        private List<MonetaryDisTributionVO> monetaryDistributionVOs;
    }

}