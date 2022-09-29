package cn.youai.xiuzhen.stat.controller;

import cn.youai.xiuzhen.stat.entity.LogPlayer;
import cn.youai.xiuzhen.stat.service.ILogPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 玩家Log
 * @date 2021-01-14
 */
@Slf4j
@RestController
@RequestMapping("game/logPlayer")
public class LogPlayerController extends JeecgController<LogPlayer, ILogPlayerService> {

    @Override
    protected boolean isReadOnly() {
        return true;
    }

    @AutoLog(value = "玩家Log-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LogPlayer entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "玩家Log-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "玩家Log-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogPlayer entity) {
        return super.exportXls(request, entity, LogPlayer.class, "玩家Log");
    }

}
