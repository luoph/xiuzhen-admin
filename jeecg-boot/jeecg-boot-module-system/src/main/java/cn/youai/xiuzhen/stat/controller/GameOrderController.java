package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.model.RangeValue;
import cn.youai.xiuzhen.core.controller.SimplePageController;
import cn.youai.xiuzhen.game.entity.GameOrder;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.annotation.Readonly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Slf4j
@Readonly
@RestController
@RequestMapping("game/order")
public class GameOrderController extends SimplePageController<GameOrder> {

    @Autowired
    private IGameOrderService gameOrderService;

    /**
     * 分页列表查询
     *
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值订单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameOrder entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected IPage<GameOrder> pageList(Page<GameOrder> page, GameOrder entity, HttpServletRequest req) {
        DateRange createDateRange = PageQueryUtils.parseRange(req.getParameterMap(), "createDate");
        RangeValue<BigDecimal> payAmountRange = PageQueryUtils.parseNumberRange(req.getParameterMap(), "payAmount");
        return gameOrderService.queryList(page, entity, createDateRange, payAmountRange);
    }

    @AutoLog(value = "充值订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameOrder entity = gameOrderService.queryById(id);
        if (entity == null) {
            return Result.error("未找到记录");
        }
        return Result.ok(entity);
    }

    @AutoLog(value = "充值订单-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameOrder entity) {
        return super.exportXls(request, entity, GameOrder.class, "充值订单");
    }

}
