package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.youai.basics.model.DateRange;
import cn.youai.server.constant.AttrType;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.entity.ServerBill;
import cn.youai.xiuzhen.stat.service.IGameOrderStatService;
import cn.youai.xiuzhen.utils.QueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器流水
 *
 * @author jeecg-boot
 * @version V1.0
 * @date 2022/09/21
 */
@Slf4j
@RestController
@RequestMapping("game/stat/serverBill")
public class GameStatServerBillController {

    @Autowired
    private IGameOrderStatService gameOrderStatService;

    @RequestMapping("/list")
    @AutoLog(value = "服务器流水-列表查询")
    public Result<?> list(ServerBill entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        Page<ServerBill> page = new Page<>(pageNo, pageSize);
        // 服务器空校验
        if (StringUtils.isEmpty(entity.getChannel())
                && (entity.getServerId() == null || entity.getServerId() < 0)) {
            return Result.ok("请选择渠道或者区服id");
        }

        // 如果指定 游戏服id，则清除渠道信息
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            entity.setChannel(null);
        }

        IPage<ServerBill> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "服务器流水-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, ServerBill entity) {
        Page<ServerBill> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<ServerBill> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), ServerBill.class, "战力日志");
    }

    private IPage<ServerBill> pageList(Page<ServerBill> page, ServerBill entity, HttpServletRequest req) {
        // 默认查询当天
        DateRange dateRange = QueryUtils.parseRange(req.getParameterMap(), "countDate");

        BigDecimal amount = BigDecimal.ZERO;
        ServerBill serverBill = new ServerBill().setStartDate(dateRange.getStart()).setEndDate(dateRange.getEnd());
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            // 按照游戏服维度统计
            serverBill.setChannel(StatisticType.DEFAULT_CHANNEL).setServerId(entity.getServerId());
            amount = gameOrderStatService.serverRangeAmount(entity.getServerId(), dateRange.getStart(), dateRange.getEnd());
        } else {
            // 按照渠道维度统计
            serverBill.setChannel(entity.getChannel()).setServerId(StatisticType.DEFAULT_SERVER_ID);
            amount = gameOrderStatService.channelRangeAmount(entity.getChannel(), dateRange.getStart(), dateRange.getEnd());
        }
        serverBill.setTotalAmount(amount);

        List<ServerBill> records = CollUtil.newArrayList(serverBill);
        IPage<ServerBill> pageList = new Page<>();
        pageList.setPages(1).setTotal(1).setCurrent(1).setSize(1).setCurrent(1).setRecords(records);
        return pageList;
    }
}
