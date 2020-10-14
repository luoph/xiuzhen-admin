package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.game.entity.RechargeOrder;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IRechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 今日礼包
 * @date 2020-10-13
 */
@Slf4j
@RestController
@RequestMapping("game/rechargeOrder")
public class RechargeOrderController extends JeecgController<RechargeOrder, IRechargeOrderService> {

    @Autowired
    private IRechargeOrderService rechargeOrderService;

    @Autowired
    private IGameChannelService gameChannelService;

    /**
     * 分页列表查询
     *
     * @param rechargeOrder 数据实体
     * @param pageNo        页码
     * @param pageSize      分页大小
     * @param req           请求
     * @return {@linkplain Result}
     */
    @AutoLog(value = "今日礼包-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(RechargeOrder rechargeOrder,
                                   @RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        QueryWrapper<RechargeOrder> queryWrapper = QueryGenerator.initQueryWrapper(rechargeOrder, req.getParameterMap());
        Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
        IPage<RechargeOrder> pageList = rechargeOrderService.page(page, queryWrapper);
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<RechargeOrder> rechargeOrders = rechargeOrderService.queryTodayGift(rangeDateBegin, rangeDateEnd, days, serverId, channel);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param rechargeOrder 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "今日礼包-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody RechargeOrder rechargeOrder) {
        rechargeOrderService.save(rechargeOrder);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param rechargeOrder 数据实体
     * @return {@linkplain Result}
     */
    @AutoLog(value = "今日礼包-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody RechargeOrder rechargeOrder) {
        rechargeOrderService.updateById(rechargeOrder);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "今日礼包-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        rechargeOrderService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id列表，使用','分割的字符串
     * @return {@linkplain Result}
     */
    @AutoLog(value = "今日礼包-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.rechargeOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 实体id
     * @return {@linkplain Result}
     */
    @AutoLog(value = "今日礼包-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        RechargeOrder rechargeOrder = rechargeOrderService.getById(id);
        if (rechargeOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(rechargeOrder);
    }

    /**
     * 导出excel
     *
     * @param request       请求
     * @param rechargeOrder 实体
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RechargeOrder rechargeOrder) {
        return super.exportXls(request, rechargeOrder, RechargeOrder.class, "今日礼包");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  请求
     * @param response 响应
     * @return {@linkplain Result}
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, RechargeOrder.class);
    }

}
