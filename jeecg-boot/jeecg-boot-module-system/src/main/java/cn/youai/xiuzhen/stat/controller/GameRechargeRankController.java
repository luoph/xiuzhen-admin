package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.stat.entity.GameRechargeRank;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Slf4j
@RestController
@RequestMapping("game/rechargeRank")
public class GameRechargeRankController {

    @Autowired
    private IGameOrderService orderStatService;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @GetMapping(value = "/list")
    @AutoLog(value = "付费排行-列表查询")
    public Result<?> queryPageList(GameRechargeRank entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameRechargeRank> page = new Page<>(pageNo, pageSize);
        return Result.ok(pageList(page, entity, req));
    }

    @AutoLog(value = "付费排行-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, GameRechargeRank entity) {
        Page<GameRechargeRank> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<GameRechargeRank> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), GameRechargeRank.class, "付费排行");
    }

    private IPage<GameRechargeRank> pageList(Page<GameRechargeRank> page, GameRechargeRank entity, HttpServletRequest req) {
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        IPage<GameRechargeRank> pageList = orderStatService.queryRechargeRankList(page, entity, dateRange);
        Set<Long> orderIdList = pageList.getRecords().stream().map(GameRechargeRank::getOrderId).collect(Collectors.toSet());
        List<GameOrder> lastOrders = orderStatService.queryByIds(orderIdList);
        Map<Long, GameOrder> orderMap = lastOrders.stream().collect(Collectors.toMap(GameOrder::getId, Function.identity(), (key1, key2) -> key2));

        Date now = DateUtils.now();
        int rank = (int) ((pageList.getCurrent() - 1) * pageList.getSize()) + 1;
        for (GameRechargeRank t : pageList.getRecords()) {
            t.setRank(rank++)
                    .setPlayDays(DateUtils.daysBetween(t.getCreateTime(), now))
                    .setLastLoginDays(DateUtils.daysBetween(t.getLastLoginTime(), now))
                    .setLastPayDays(DateUtils.daysBetween(t.getLastPayTime(), now));

            GameOrder lastOrder = orderMap.get(t.getOrderId());
            if (lastOrder != null) {
                t.setLastPay(lastOrder.getPayAmount());
            }
        }
        return pageList;
    }

}
