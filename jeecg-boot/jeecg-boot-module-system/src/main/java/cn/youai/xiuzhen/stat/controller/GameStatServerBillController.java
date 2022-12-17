package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.basics.utils.StringUtils;
import cn.youai.xiuzhen.game.entity.GameChannel;
import cn.youai.xiuzhen.game.entity.GameServerVO;
import cn.youai.xiuzhen.game.service.IGameChannelServerService;
import cn.youai.xiuzhen.game.service.IGameChannelService;
import cn.youai.xiuzhen.stat.constant.StatisticType;
import cn.youai.xiuzhen.stat.entity.ServerBill;
import cn.youai.xiuzhen.stat.service.IGameOrderService;
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
    private IGameOrderService gameOrderStatService;

    @Autowired
    private IGameChannelServerService channelServerService;

    @Autowired
    private IGameChannelService channelService;

    @RequestMapping("/list")
    @AutoLog(value = "服务器流水-列表查询")
    public Result<?> list(ServerBill entity,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        // 服务器空校验
        Page<ServerBill> page = new Page<>(pageNo, pageSize);
        IPage<ServerBill> pageList = pageList(page, entity, req);
        return Result.ok(pageList);
    }

    @AutoLog(value = "服务器流水-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req, ServerBill entity) {
        Page<ServerBill> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<ServerBill> pageList = pageList(page, entity, req);
        return ExcelUtils.exportXls(pageList, req.getParameter("selections"), ServerBill.class, "服务器流水");
    }

    private IPage<ServerBill> pageList(Page<ServerBill> page, ServerBill entity, HttpServletRequest req) {
        // 默认查询当天
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "countDate");

        BigDecimal amount = BigDecimal.ZERO;
        List<ServerBill> records = new ArrayList<>();
        // 查询单个游戏服
        if (entity.getServerId() != null && entity.getServerId() > 0) {
            // 按照游戏服维度统计
            records.add(getServerBill(entity.getServerId(), dateRange));
        } else {
            if (StringUtils.isNotBlank(entity.getChannel())) {
                List<GameServerVO> serverList = channelServerService.selectServerList(entity.getChannel());
                for (GameServerVO server : serverList) {
                    ServerBill serverBill = getServerBill(server.getId(), dateRange);
                    if (StringUtils.isNotEmpty(entity.getChannel())) {
                        serverBill.setChannel(entity.getChannel());
                    }
                    records.add(serverBill);
                }

                // 按照渠道维度统计
                records.add(getServerBill(entity.getChannel(), dateRange));
            } else {
                List<GameChannel> channelList = channelService.list();
                for (GameChannel channel : channelList) {
                    records.add(getServerBill(channel.getSimpleName(), dateRange));
                }

                // 汇总
                records.add(getServerBill("", dateRange).setChannel("所有渠道"));
            }
        }

        return PageQueryUtils.makePage(records);
    }

    private ServerBill getServerBill(int serverId, DateRange dateRange) {
        ServerBill serverBill = new ServerBill().setStartDate(dateRange.getStart()).setEndDate(dateRange.getEnd());
        serverBill.setChannel(StatisticType.DEFAULT_CHANNEL).setServerId(serverId);
        BigDecimal amount = gameOrderStatService.serverRangeAmount(serverId, dateRange.getStart(), dateRange.getEnd());
        serverBill.setTotalAmount(amount);
        return serverBill;
    }

    private ServerBill getServerBill(String channel, DateRange dateRange) {
        ServerBill serverBill = new ServerBill().setStartDate(dateRange.getStart()).setEndDate(dateRange.getEnd());
        serverBill.setChannel(channel).setServerId(StatisticType.DEFAULT_SERVER_ID);
        BigDecimal amount = gameOrderStatService.channelRangeAmount(channel, dateRange.getStart(), dateRange.getEnd());
        serverBill.setTotalAmount(amount);
        return serverBill;
    }
}
