package org.jeecg.modules.game.controller;

import cn.youai.xiuzhen.utils.DateUtils;
import cn.youai.xiuzhen.utils.NumberUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.game.entity.GameStatOrder;
import org.jeecg.modules.game.service.IGameStatOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description game_stat_order
 * @date 2021-04-20
 */
@Slf4j
@RestController
@RequestMapping("game/gameStatOrder")
public class GameStatOrderController extends JeecgController<GameStatOrder, IGameStatOrderService> {

    @Autowired
    private IGameStatOrderService gameStatOrderService;

    /**
     * 分页列表查询
     *
     * @param gameStatOrder 数据实体
     * @param pageNo        页码
     * @param pageSize      分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_stat_order-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatOrder gameStatOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (NumberUtils.isZero(gameStatOrder.getServerId())) {
            return Result.error("请选择服务器！");
        }

        if (gameStatOrder.getStartDate() == null || gameStatOrder.getEndDate() == null) {
            Date now = DateUtils.now();
            gameStatOrder.setEndDate(now);
            if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_7_DAYS == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.minusDays(now, CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_7_DAYS - 1));
            } else if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_15_DAYS == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.minusDays(now, CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_15_DAYS - 1));
            } else if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_MONTH == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.startTimeOfMonth(now));
            } else if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_2_MONTHS == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.startTimeOfMonth(DateUtils.minusMonths(now, 1)));
            } else {
                return Result.error("请选择查询日期！");
            }
        }

        IPage<GameStatOrder> pageList = gameStatOrderService.queryGameStatOrderList(gameStatOrder, pageNo, pageSize);
        if (pageList == null) {
            pageList = new Page<>();
            pageList.setRecords(Collections.EMPTY_LIST);
        }
        return Result.ok(pageList);
    }

    /**
     * 导出excel
     *
     * @param request       请求
     * @param gameStatOrder 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatOrder gameStatOrder) {
        if (NumberUtils.isZero(gameStatOrder.getServerId())) {
            return new ModelAndView();
        }

        if (gameStatOrder.getStartDate() == null || gameStatOrder.getEndDate() == null) {
            Date now = DateUtils.now();
            gameStatOrder.setEndDate(now);
            if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_7_DAYS == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.minusDays(now, CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_7_DAYS - 1));
            } else if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_15_DAYS == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.minusDays(now, CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_15_DAYS - 1));
            } else if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_MONTH == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.startTimeOfMonth(now));
            } else if (CommonConstant.QUERY_CONDITION_DATE_TYPE_LAST_2_MONTHS == gameStatOrder.getDaysType()) {
                gameStatOrder.setStartDate(DateUtils.startTimeOfMonth(DateUtils.minusMonths(now, 1)));
            } else {
                return new ModelAndView();
            }
        }

        IPage<GameStatOrder> pageList = gameStatOrderService.queryGameStatOrderList(gameStatOrder, 1, Integer.MAX_VALUE);
        if (pageList != null) {
            List<GameStatOrder> list = pageList.getRecords();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

            return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), GameStatOrder.class, "充值统计");
        }
        return new ModelAndView();
    }

}