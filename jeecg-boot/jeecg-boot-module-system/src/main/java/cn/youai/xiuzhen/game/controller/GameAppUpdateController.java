package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameAppUpdate;
import cn.youai.xiuzhen.game.service.IGameAppUpdateService;
import io.swagger.annotations.ApiOperation;
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
 * @description 客户端版本
 * @date 2021-06-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameAppUpdate")
public class GameAppUpdateController extends JeecgController<GameAppUpdate, IGameAppUpdateService> {

    @AutoLog(value = "客户端版本-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameAppUpdate entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "客户端版本-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameAppUpdate entity) {
        return super.add(entity);
    }

    @AutoLog(value = "客户端版本-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameAppUpdate entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "客户端版本-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "客户端版本-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "客户端版本-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "客户端版本-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameAppUpdate gameAppUpdate) {
        return super.exportXls(request, gameAppUpdate, GameAppUpdate.class, "客户端版本");
    }

    @AutoLog(value = "客户端版本-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameAppUpdate.class);
    }

    @AutoLog(value = "客户端版本-刷新版本配置")
    @ApiOperation(value = "客户端版本-刷新版本配置", notes = "客户端版本-刷新版本配置")
    @GetMapping(value = "/updateConfig")
    public Result<?> updateConfig(HttpServletRequest req) {
        try {
            service.updateConfig();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.ok("刷新成功");
    }
}
