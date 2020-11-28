package org.jeecg.modules.player.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.player.entity.GameOrder;
import org.jeecg.modules.player.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 充值订单
 * @date 2020-01-05
 */
@Slf4j
@RestController
@RequestMapping("player/payOrder")
public class PayOrderController extends JeecgController<GameOrder, IPayOrderService> {

    @Autowired
    private IPayOrderService payOrderService;

    /**
     * 分页列表查询
     *
     * @param gameOrder 数据实体
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @param req       请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值订单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameOrder gameOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GameOrder> queryWrapper = QueryGenerator.initQueryWrapper(gameOrder, req.getParameterMap());
        Page<GameOrder> page = new Page<>(pageNo, pageSize);
        IPage<GameOrder> pageList = payOrderService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

//    /**
//     * 添加
//     *
//     * @param payOrder 数据实体
//     * @return {@linkplain Result}
//     */
//    @AutoLog(value = "充值订单-添加")
//    @PostMapping(value = "/add")
//    public Result<?> add(@RequestBody PayOrder payOrder) {
//        payOrderService.save(payOrder);
//        return Result.ok("添加成功！");
//    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "充值订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        GameOrder gameOrder = payOrderService.getById(id);
        if (gameOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gameOrder);
    }

    /**
     * 导出excel
     *
     * @param request   请求
     * @param gameOrder 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameOrder gameOrder) {
        return super.exportXls(request, gameOrder, GameOrder.class, "充值订单");
    }
}
