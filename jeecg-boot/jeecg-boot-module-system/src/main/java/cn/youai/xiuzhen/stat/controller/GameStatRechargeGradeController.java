package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.xiuzhen.game.constant.RechargeGrade;
import cn.youai.xiuzhen.stat.entity.GameStatPlayerRechargeAmount;
import cn.youai.xiuzhen.stat.entity.GameStatRechargeGrade;
import cn.youai.xiuzhen.stat.entity.GameStatRechargeSum;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_order
 * @date 2020-09-29
 */
@Slf4j
@RestController
@RequestMapping("game/stat/rechargeGrade")
public class GameStatRechargeGradeController {

    @Autowired
    private IGameOrderStatService orderStatService;

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "付费结构-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatRechargeGrade entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameStatRechargeGrade> page = new Page<>(pageNo, pageSize);
        IPage<GameStatRechargeGrade> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "付费结构-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, GameStatRechargeGrade entity) {
        Page<GameStatRechargeGrade> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<GameStatRechargeGrade> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), GameStatRechargeGrade.class, "付费结构");
    }

    private IPage<GameStatRechargeGrade> pageList(Page<GameStatRechargeGrade> page, GameStatRechargeGrade entity, HttpServletRequest req) {
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        int serverId = entity.getServerId() != null ? entity.getServerId() : 0;
        if (serverId > 0) {
            entity.setChannel(null);
        }

        GameStatRechargeSum rechargeSum = orderStatService.queryStatRechargeGradeSum(entity.getChannel(), serverId,
                dateRange.getStart(), dateRange.getEnd());
        List<GameStatPlayerRechargeAmount> list = orderStatService.queryPlayerRechargeAmount(entity.getChannel(), serverId,
                dateRange.getStart(), dateRange.getEnd());

        Map<RechargeGrade, List<GameStatPlayerRechargeAmount>> map = new HashMap<>();
        list.forEach(t -> {
            RechargeGrade grade = RechargeGrade.valueOf(t.getPayAmount());
            if (grade != null) {
                map.computeIfAbsent(grade, k -> new ArrayList<>()).add(t);
            }
        });

        List<GameStatRechargeGrade> records = new ArrayList<>();
        map.forEach((k, v) -> records.add(sumGrade(k, rechargeSum, v)));
        records.sort(Comparator.comparing(GameStatRechargeGrade::getGrade));
        if (entity.getGrade() != null && entity.getGrade() > 0) {
            GameStatRechargeGrade grade = records.stream().filter(t -> Objects.equals(t.getGrade(), entity.getGrade()))
                    .findFirst().orElse(null);
            return PageQueryUtils.makePage(grade);
        }

        return PageQueryUtils.makePage(records);
    }

    private GameStatRechargeGrade sumGrade(RechargeGrade grade, GameStatRechargeSum sum, List<GameStatPlayerRechargeAmount> list) {
        int playerNumSum = list.size();
        int payNum = 0;
        BigDecimal payAmountSum = BigDecimal.ZERO;
        for (GameStatPlayerRechargeAmount t : list) {
            payAmountSum = payAmountSum.add(t.getPayAmount());
            payNum += t.getPayNum();
        }
        return new GameStatRechargeGrade().setGrade(grade.getType())
                .setPayNum(payNum).setPlayerNum(playerNumSum)
                .setPayAmount(payAmountSum)
                .setTotalPayNum(sum.getTotalNum())
                .setTotalAmount(sum.getTotalAmount())
                .setTotalPlayerNum(sum.getTotalPlayerNum());
    }

}
