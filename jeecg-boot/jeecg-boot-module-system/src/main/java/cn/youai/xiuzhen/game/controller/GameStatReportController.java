package cn.youai.xiuzhen.game.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.youai.basics.model.Response;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.stat.service.ILogCmdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 游戏信息
 * @date 2019-12-11
 */
@Slf4j
@RestController
@RequestMapping("/game/stat/report")
public class GameStatReportController {

    @Value("${app.stat.cmd-cost-time:500}")
    private int costTime;

    @Autowired
    private ILogCmdService logCmdService;

    @Autowired
    private IGameServerService gameServerService;

    @RequestMapping(value = "/logCmd")
    public Response logCmd(@RequestParam(value = "serverId", required = false, defaultValue = "0") Integer serverId,
                           @RequestParam(value = "start") String start,
                           @RequestParam(value = "end") String end) {
        Date startDate = DateUtils.dateOnly(DateUtil.parseDate(start));
        Date endDate = DateUtils.dateOnly(DateUtil.parseDate(end));
        if (endDate.before(startDate)) {
            return Response.failure("param invalid");
        }

        Date current = startDate;
        while (!current.after(endDate)) {
            List<Integer> serverIds = serverId > 0 ? CollUtil.newArrayList(serverId) : gameServerService.selectRunningServerIds(current);
            for (Integer sid : serverIds) {
                logCmdService.genReport(sid, current, costTime);
            }
            current = DateUtils.addDays(current, 1);
        }

        return Response.success();
    }

}
