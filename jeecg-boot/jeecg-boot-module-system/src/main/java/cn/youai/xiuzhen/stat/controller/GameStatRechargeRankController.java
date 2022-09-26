package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameStatRechargeRank;
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
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值用户排行数据统计
 * @date 2020-10-09
 */
@Slf4j
@RestController
@RequestMapping("game/stat/rechargeRank")
public class GameStatRechargeRankController {

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
    @AutoLog(value = "玩家充值排行-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatRechargeRank entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<GameStatRechargeRank> page = new Page<>(pageNo, pageSize);
        IPage<GameStatRechargeRank> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "玩家充值排行-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, GameStatRechargeRank entity) {
        Page<GameStatRechargeRank> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<GameStatRechargeRank> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), GameStatRechargeRank.class, "玩家充值排行");
    }

    private IPage<GameStatRechargeRank> pageList(Page<GameStatRechargeRank> page, GameStatRechargeRank entity, HttpServletRequest req) {
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        int serverId = entity.getServerId() != null ? entity.getServerId() : 0;
        Date now = DateUtils.now();
        List<GameStatRechargeRank> records = orderStatService.queryRechargeRankList(entity.getChannel(), serverId, dateRange.getStart(), dateRange.getEnd());
        int rank = 1;
        for (GameStatRechargeRank t : records) {
            t.setRank(rank++)
                    .setLastLoginDays(DateUtils.daysBetween(t.getLastLoginTime(), now))
                    .setLastPayDays(DateUtils.daysBetween(t.getLastPayTime(), now));
        }

        if (entity.getPlayerId() != null) {
            GameStatRechargeRank target = records.stream().filter(t -> t.getPlayerId().equals(entity.getPlayerId())).findAny().orElse(null);
            if (target != null) {
                return PageQueryUtils.makePage(CollUtil.newArrayList(target));
            }
            return PageQueryUtils.makePage(CollUtil.newArrayList());
        }
        return PageQueryUtils.makePage(records);
    }

}
