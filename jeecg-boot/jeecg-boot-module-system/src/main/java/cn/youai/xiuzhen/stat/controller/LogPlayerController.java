package cn.youai.xiuzhen.stat.controller;

import cn.youai.xiuzhen.stat.entity.LogPlayer;
import cn.youai.xiuzhen.stat.service.ILogPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description log_player
 * @date 2021-01-14
 */
@Slf4j
@RestController
@RequestMapping("game/logPlayer")
public class LogPlayerController extends JeecgController<LogPlayer, ILogPlayerService> {

    @AutoLog(value = "log_player-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LogPlayer entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "log_player-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LogPlayer entity) {
        return super.add(entity);
    }

    @AutoLog(value = "log_player-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LogPlayer entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "log_player-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "log_player-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "log_player-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogPlayer entity) {
        return super.exportXls(request, entity, LogPlayer.class, "log_player");
    }

    @AutoLog(value = "XXX-导入") // TODO
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LogPlayer.class);
    }

}
