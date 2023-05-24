package cn.youai.xiuzhen.stat.controller;

import cn.youai.basics.model.DateRange;
import cn.youai.server.exception.AppException;
import cn.youai.server.utils.DateUtils;
import cn.youai.xiuzhen.core.controller.SimplePageController;
import cn.youai.xiuzhen.stat.entity.LogChat;
import cn.youai.xiuzhen.stat.service.ILogChatService;
import cn.youai.xiuzhen.utils.PageQueryUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.annotation.Readonly;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LogChatController extends SimplePageController<LogChat> {

    @Autowired
    private ILogChatService logChatService;

    @AutoLog(value = "聊天日志-列表查询")
    @GetMapping(value = "/list")
    @PermissionData(value = "game/GameLogChatList")
    public Result<?> queryPageList(LogChat entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @Override
    protected IPage<LogChat> pageList(Page<LogChat> page, LogChat entity, HttpServletRequest req) throws AppException {
        DateRange dateRange = PageQueryUtils.parseRange(req.getParameterMap(), "createDate");
        if (dateRange.getEnd() == null) {
            dateRange.setEnd(DateUtils.todayDate());
        }
        if (dateRange.getStart() == null) {
            dateRange.setStart(DateUtils.todayDate());
        }
        int days = DateUtils.daysBetween(dateRange.getStart(), dateRange.getEnd());
        if (days > 30) {
            throw new AppException("不支持超过30天的数据查询");
        }
        return logChatService.queryList(page, entity, dateRange);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogChat logChat) {
        return super.exportXls(request, logChat, LogChat.class, "聊天日志");
    }
}
