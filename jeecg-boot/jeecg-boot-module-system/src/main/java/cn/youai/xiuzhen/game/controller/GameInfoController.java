package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameInfo;
import cn.youai.xiuzhen.game.service.IGameInfoService;
import io.swagger.annotations.Api;
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
 * @description 游戏信息
 * @date 2019-12-11
 */
@Slf4j
@RestController
@Api(tags = "游戏信息")
@RequestMapping("/game/info")
public class GameInfoController extends JeecgController<GameInfo, IGameInfoService> {

    @AutoLog(value = "游戏信息-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameInfo entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "游戏信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameInfo entity) {
        return super.add(entity);
    }

    @AutoLog(value = "游戏信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameInfo entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "游戏信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "游戏信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏信息-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameInfo gameInfo) {
        return super.exportXls(request, gameInfo, GameInfo.class, "游戏信息");
    }

    @AutoLog(value = "游戏信息-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameInfo.class);
    }

    @AutoLog(value = "游戏信息-刷新配置")
    @GetMapping(value = "/refreshConfig")
    public Result<?> refreshConfig(HttpServletRequest req) {
        try {
            service.refreshConfig();
        } catch (Exception e) {
            log.error("refreshConfig error", e);
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }
}
