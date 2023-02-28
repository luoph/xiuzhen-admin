package cn.youai.xiuzhen.game.controller;

import cn.youai.server.springboot.component.OkHttpHelper;
import cn.youai.xiuzhen.game.entity.GameSensitiveWord;
import cn.youai.xiuzhen.game.service.IGameSensitiveWordService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 敏感词
 * @date 2023-01-10
 */
@Slf4j
@RestController
@RequestMapping("game/sensitiveWord")
public class GameSensitiveWordController extends JeecgController<GameSensitiveWord, IGameSensitiveWordService> {

    @Value("${app.url.game-center}")
    private String gameCenterUrl;

    @AutoLog(value = "敏感词-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameSensitiveWord entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "敏感词-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameSensitiveWord entity) {
        return super.add(entity);
    }

    @AutoLog(value = "敏感词-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameSensitiveWord entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "敏感词-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "敏感词-刷新缓存")
    @GetMapping(value = "/refresh")
    public Result<?> refresh() {
        OkHttpHelper.get(gameCenterUrl + "/setting/refreshSensitiveWords");
        return Result.ok("刷新成功!");
    }

    @AutoLog(value = "敏感词-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "敏感词-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "敏感词-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameSensitiveWord entity) {
        return super.exportXls(request, entity, GameSensitiveWord.class, "敏感词");
    }

    @AutoLog(value = "敏感词-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameSensitiveWord.class, "敏感词");
    }

}
