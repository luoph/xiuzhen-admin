package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.cache.GameSettingCache;
import cn.youai.xiuzhen.game.entity.GameSetting;
import cn.youai.xiuzhen.game.service.IGameSettingService;
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
 * @description 游戏设置
 * @date 2020-01-04
 */
@Slf4j
@RestController
@RequestMapping("game/gameSetting")
public class GameSettingController extends JeecgController<GameSetting, IGameSettingService> {

    @AutoLog(value = "游戏设置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameSetting entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "游戏设置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameSetting entity) {
        Result<?> result = super.add(entity);
        if (result.isSuccess()) {
            GameSettingCache.getInstance().put(entity.getDictKey(), entity);
        }
        return result;
    }

    @AutoLog(value = "游戏设置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameSetting entity) {
        Result<?> result = super.edit(entity);
        if (result.isSuccess()) {
            GameSettingCache.getInstance().put(entity.getDictKey(), entity);
        }
        return result;
    }

    @AutoLog(value = "游戏设置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        Result<?> result = super.queryById(id);
        if (!result.isSuccess()) {
            return result;
        }
        result = super.delete(id);
        if (result.isSuccess()) {
            GameSetting entity = (GameSetting) result.getResult();
            GameSettingCache.getInstance().remove(entity.getDictKey());
        }
        return result;
    }

    @AutoLog(value = "游戏设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        Result<?> result = super.deleteBatch(ids);
        if (result.isSuccess()) {
            GameSettingCache.getInstance().clear();
            GameSettingCache.getInstance().loadAll();
        }
        return result;
    }

    @AutoLog(value = "游戏设置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏设置-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameSetting entity) {
        return super.exportXls(request, entity, GameSetting.class, "游戏设置");
    }

    @AutoLog(value = "游戏设置-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        Result<?> result = super.importExcel(request, response, GameSetting.class);
        if (result.isSuccess()) {
            GameSettingCache.getInstance().clear();
            GameSettingCache.getInstance().loadAll();
        }
        return result;
    }

    @AutoLog(value = "游戏设置-刷新")
    @GetMapping(value = "/refresh")
    public Result<?> refresh() {
        GameSettingCache.getInstance().clear();
        GameSettingCache.getInstance().loadAll();
        return Result.ok("刷新成功!");
    }
}
