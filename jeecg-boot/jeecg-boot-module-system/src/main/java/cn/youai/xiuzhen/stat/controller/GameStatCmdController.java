package cn.youai.xiuzhen.stat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.cmd.Message.MessageId;
import cn.youai.xiuzhen.game.entity.GameStatCmd;
import cn.youai.xiuzhen.game.service.IGameServerService;
import cn.youai.xiuzhen.game.service.IGameStatCmdService;
import cn.youai.xiuzhen.stat.service.ILogCmdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @description 接口统计
 * @date 2023-07-13
 */
@Slf4j
@RestController
@RequestMapping("/game/stat/cmd")
public class GameStatCmdController extends JeecgController<GameStatCmd, IGameStatCmdService> {

    @Value("${app.stat.cmd-cost-time:500}")
    private int costTime;

    @Autowired
    private ILogCmdService logCmdService;

    @Autowired
    private IGameServerService gameServerService;

    @Override
    @AutoLog(value = "接口统计-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameStatCmd entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GameStatCmd> pageList = pageList(entity, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    @Override
    protected QueryWrapper<GameStatCmd> prepareQuery(GameStatCmd entity, HttpServletRequest request) {
        return super.prepareQuery(entity, request).orderByDesc("create_date", "cost_time", "num");
    }

    @AutoLog(value = "接口统计-更新")
    @GetMapping(value = "/update")
    public Result<?> update(GameStatCmd entity, HttpServletRequest req) {
        Date todayDate = DateUtils.todayDate();
        List<Integer> serverIds = gameServerService.selectRunningServerIds(todayDate);
        if (CollUtil.isNotEmpty(serverIds)) {
            for (Integer sid : serverIds) {
                logCmdService.genReport(sid, todayDate, costTime);
            }
        }
        return Result.ok("更新成功");
    }

    @AutoLog(value = "接口统计-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameStatCmd entity) {
        return super.exportXls(request, entity, GameStatCmd.class, "接口统计");
    }

    @Override
    protected void onload(List<GameStatCmd> pageList) {
        for (GameStatCmd cmd : pageList) {
            MessageId messageId = MessageId.valueOf(cmd.getMsgId());
            if (messageId != null) {
                cmd.setMsgName(StrUtil.removePrefix(messageId.name(), "Id"));
            }
        }
    }
}
