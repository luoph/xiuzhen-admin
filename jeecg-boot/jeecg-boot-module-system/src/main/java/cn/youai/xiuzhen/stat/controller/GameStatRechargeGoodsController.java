package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.constant.RechargeGoodsGroup;
import cn.youai.xiuzhen.game.entity.GameRechargeGoods;
import cn.youai.xiuzhen.game.service.IGameRechargeGoodsService;
import cn.youai.xiuzhen.stat.entity.GameStatRechargeGoods;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 直充道具数据统计
 * @date 2020-09-29
 */
@Slf4j
@RestController
@RequestMapping("game/stat/rechargeGoods")
public class GameStatRechargeGoodsController {

    @Autowired
    private IGameOrderStatService orderStatService;

    @Autowired
    private IGameRechargeGoodsService rechargeGoodsService;

    @AutoLog(value = "直充道具统计-列表查询")
    @RequestMapping("/list")
    public Result<?> list(GameStatRechargeGoods entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        Page<GameStatRechargeGoods> page = new Page<>(pageNo, pageSize);
        // 服务器空校验
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok(page);
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        IPage<GameStatRechargeGoods> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "直充道具统计-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, GameStatRechargeGoods entity) {
        Page<GameStatRechargeGoods> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<GameStatRechargeGoods> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), GameStatRechargeGoods.class, "直充道具统计");
    }

    private IPage<GameStatRechargeGoods> pageList(Page<GameStatRechargeGoods> page, GameStatRechargeGoods entity, HttpServletRequest req) {
        // 默认查询当天
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");
        int serverId = entity.getServerId() != null ? entity.getServerId() : 0;
        if (serverId > 0) {
            entity.setChannel(null);
        }

        if (entity.getRechargeGroup() == null) {
            entity.setRechargeGroup(RechargeGoodsGroup.ALL.getType());
        }

        RechargeGoodsGroup rechargeGroup = RechargeGoodsGroup.valueOf(entity.getRechargeGroup());
        GameStatRechargeSum rechargeSum = orderStatService.queryStatRechargeGoodsSum(entity.getChannel(),
                entity.getServerId(), rechargeGroup.getType(), dateRange.getStart(), dateRange.getEnd());
        List<GameStatRechargeGoods> records = orderStatService.queryStatRechargeGoods(entity.getChannel(),
                entity.getServerId(), rechargeGroup.getType(), dateRange.getStart(), dateRange.getEnd());

        records.forEach(t -> t.setTotalAmount(rechargeSum.getTotalAmount())
                .setTotalPlayerNum(rechargeSum.getTotalPlayerNum())
                .setTotalNum(rechargeSum.getTotalNum()));
        records.sort(Comparator.comparing(GameStatRechargeGoods::getPayNum, Comparator.reverseOrder()));
        return PageQueryUtils.makePage(records);
    }

}
