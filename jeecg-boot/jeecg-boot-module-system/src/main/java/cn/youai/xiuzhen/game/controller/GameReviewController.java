package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameReview;
import cn.youai.xiuzhen.game.service.IGameReviewService;
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
 * @description 游戏审核
 * @date 2023-02-16
 */
@Slf4j
@RestController
@Api(tags = "游戏审核")
@RequestMapping("/game/review")
public class GameReviewController extends JeecgController<GameReview, IGameReviewService> {

    @AutoLog(value = "游戏审核-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameReview entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "游戏审核-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameReview entity) {
        return super.add(entity);
    }

    @AutoLog(value = "游戏审核-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameReview entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "游戏审核-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "游戏审核-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "游戏审核-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "游戏审核-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameReview gameInfo) {
        return super.exportXls(request, gameInfo, GameReview.class, "游戏审核");
    }

    @AutoLog(value = "游戏审核-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameReview.class);
    }

}
