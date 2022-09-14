package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.utils.NumberUtils;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.stat.entity.GameStatOrder;
import cn.youai.xiuzhen.stat.service.IGameStatOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.util.ExcelUtils;
import org.jeecg.common.system.vo.LoginUser;
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
 * @description game_stat_order
 * @date 2021-04-20
 */
@Slf4j
@RestController
@RequestMapping("game/gameStatOrder")
public class GameStatOrderController extends JeecgController<GameStatOrder, IGameStatOrderService> {

    /**
     * 分页列表查询
     *
     * @param entity   数据实体
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "game_stat_order-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatOrder entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (NumberUtils.isZero(entity.getServerId())) {
            return Result.error("请选择服务器！");
        }

        if (entity.getStartDate() == null || entity.getEndDate() == null) {
            Date now = DateUtils.now();
            entity.setEndDate(now);
            entity.setStartDate(DateUtils.addDays(now, -entity.getDaysType()));
        }

        IPage<GameStatOrder> pageList = service.queryGameStatOrderList(entity, pageNo, pageSize);
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
            gameStatOrder.setStartDate(DateUtils.addDays(now, -gameStatOrder.getDaysType()));
        }

        IPage<GameStatOrder> pageList = service.queryGameStatOrderList(gameStatOrder, 1, Integer.MAX_VALUE);
        if (pageList != null) {
            List<GameStatOrder> list = pageList.getRecords();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

            return ExcelUtils.exportXls(sysUser.getRealname(), list, request.getParameter("selections"), GameStatOrder.class, "充值统计");
        }
        return new ModelAndView();
    }

}
