package org.jeecg.modules.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.game.entity.GameChalcedonyOrder;
import org.jeecg.modules.game.entity.RechargeOrder;
import org.jeecg.modules.game.service.IGameChannelService;
import org.jeecg.modules.game.service.IRechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.jeecg.modules.game.constant.FairyJadeBuyType.*;

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

    /**
     * 礼包类型
     */
    private static final int[] GOODS_TYPE = {0, 1, 2, 3, 4,};

    @Autowired
    private IGameChannelService gameChannelService;


    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "普通礼包-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                   @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                   @RequestParam(name = "days", defaultValue = "0") int days,
                                   @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                   @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && days == 0) {
            return Result.ok(page);
        }
        // 如果选择开始时间和结束时间是同一天
        if (rangeDateBegin.equals(rangeDateEnd)) {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<RechargeOrder> rechargeOrders = service.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[0]);
        page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
        return Result.ok(page);
    }

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "今日礼包-列表查询")
    @GetMapping(value = "/giftBagList")
    public Result<?> giftBagList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                 @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                 @RequestParam(name = "days", defaultValue = "0") int days,
                                 @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                 @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        //改回到博东的这种方式，否则会有问题
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (days == 0) {
                return Result.error("请输入日期！");
            }
        } else {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<RechargeOrder> rechargeOrders = service.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[3]);
        page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
        return Result.ok(page);
    }

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "首充-列表查询")
    @GetMapping(value = "/firstCharge")
    public Result<?> firstCharge(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                 @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                 @RequestParam(name = "days", defaultValue = "0") int days,
                                 @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                 @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        //改回到博东的这种方式，否则会有问题
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (days == 0) {
                return Result.error("请输入日期！");
            }
        } else {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<RechargeOrder> rechargeOrders = service.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[4]);
        page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
        return Result.ok(page);
    }


    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "仙职-列表查询")
    @GetMapping(value = "/officialJob")
    public Result<?> officialJob(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                 @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                 @RequestParam(name = "days", defaultValue = "0") int days,
                                 @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                 @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        //改回到博东的这种方式，否则会有问题
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (days == 0) {
                return Result.error("请输入日期！");
            }
        } else {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<RechargeOrder> rechargeOrders = service.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[1]);
        page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
        return Result.ok(page);
    }

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "月卡-列表查询")
    @GetMapping(value = "/monthCard")
    public Result<?> monthCard(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                               @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                               @RequestParam(name = "days", defaultValue = "0") int days,
                               @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                               @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<RechargeOrder> page = new Page<>(pageNo, pageSize);
        if (StringUtils.isEmpty(rangeDateBegin) && StringUtils.isEmpty(rangeDateEnd) && serverId == 0 && channelId == 0 && days == 0) {
            return Result.ok(page);
        }
        //改回到博东的这种方式，否则会有问题
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (days == 0) {
                return Result.error("请输入日期！");
            }
        } else {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<RechargeOrder> rechargeOrders = service.queryGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, GOODS_TYPE[2]);
        page.setRecords(rechargeOrders).setTotal(rechargeOrders.size());
        return Result.ok(page);
    }

    /**
     * 特惠礼包列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "特惠礼包-列表查询")
    @GetMapping(value = "/preference")
    public Result<?> preferenceGiftList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                        @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                        @RequestParam(name = "days", defaultValue = "0") int days,
                                        @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                        @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameChalcedonyOrder> page = new Page<>(pageNo, pageSize);
        if (serverId == 0 && channelId == 0) {
            return Result.error("请选择服务器！");
        }
        //改回到博东的这种方式，否则会有问题
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (days == 0) {
                return Result.error("请输入日期！");
            }
        } else {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameChalcedonyOrder> gameChalcedonyOrders = service.queryExpendGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, DAILY_GIFT.getType().toString());
        page.setRecords(gameChalcedonyOrders).setTotal(gameChalcedonyOrders.size());
        return Result.ok(page);
    }

    /**
     * 冲榜礼包列表查询(开服礼包)
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "冲榜礼包-列表查询")
    @GetMapping(value = "/atList")
    public Result<?> atListGiftList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                    @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                    @RequestParam(name = "days", defaultValue = "0") int days,
                                    @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                    @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameChalcedonyOrder> page = new Page<>(pageNo, pageSize);
        if (serverId == 0 && channelId == 0) {
            return Result.error("请选择服务器！");
        }
        //改回到博东的这种方式，否则会有问题
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (days == 0) {
                return Result.error("请输入日期！");
            }
        } else {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameChalcedonyOrder> gameChalcedonyOrders = service.queryExpendGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, OPEN_SERVICE_GIFT.getType().toString());
        page.setRecords(gameChalcedonyOrders).setTotal(gameChalcedonyOrders.size());
        return Result.ok(page);
    }

    /**
     * 0元购列表查询
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return {@linkplain Result}
     */
    @AutoLog(value = "0元购-列表查询")
    @GetMapping(value = "/zeroBuy")
    public Result<?> zeroBuyGiftList(@RequestParam(name = "rangeDateBegin", defaultValue = "") String rangeDateBegin,
                                     @RequestParam(name = "rangeDateEnd", defaultValue = "") String rangeDateEnd,
                                     @RequestParam(name = "days", defaultValue = "0") int days,
                                     @RequestParam(name = "serverId", defaultValue = "0") Integer serverId,
                                     @RequestParam(name = "channelId", defaultValue = "0") Integer channelId,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GameChalcedonyOrder> page = new Page<>(pageNo, pageSize);
        if (serverId == 0 && channelId == 0) {
            return Result.error("请选择服务器！");
        }
        // 没有传入时间和天数返回空的数据
        if (StringUtils.isEmpty(rangeDateBegin) || StringUtils.isEmpty(rangeDateEnd)) {
            if (days == 0) {
                return Result.error("请输入日期！");
            }
        } else {
            rangeDateBegin = rangeDateBegin + " 00:00:00";
            rangeDateEnd = rangeDateEnd + " 23:59:59";
        }
        String channel = gameChannelService.queryChannelNameById(channelId);
        List<GameChalcedonyOrder> gameChalcedonyOrders = service.queryExpendGiftList(rangeDateBegin, rangeDateEnd, days, serverId, channel, ZERO_BUY.getType().toString());
        page.setRecords(gameChalcedonyOrders).setTotal(gameChalcedonyOrders.size());
        return Result.ok(page);
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


}
