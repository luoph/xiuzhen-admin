package cn.youai.xiuzhen.stat.controller;

import cn.youai.xiuzhen.game.entity.OpenServiceCampaignConsumeDetailItem;
import cn.youai.xiuzhen.stat.entity.LogChat;
import cn.youai.xiuzhen.stat.service.ILogChatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.annotation.Readonly;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 聊天日志
 * @Author: jeecg-boot
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Slf4j
@Readonly
@RestController
@RequestMapping("/game/stat/logChat")
public class LogChatController extends JeecgController<LogChat, ILogChatService> {

    @AutoLog(value = "聊天日志-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LogChat entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected QueryWrapper<LogChat> prepareQuery(LogChat entity, HttpServletRequest request) {
        return super.prepareQuery(entity, request).ne("sender_id", 0).ne("chat_type", 0);
    }

    @AutoLog(value = "聊天日志-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogChat logChat) {
        return super.exportXls(request, logChat, LogChat.class, "聊天日志");
    }
}
