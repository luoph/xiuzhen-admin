package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeThrowingEggsRank;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeThrowingEggsRankService;
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
 * @description 砸蛋榜
 * @date 2021-03-10
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeThrowingEggsRank")
public class GameCampaignTypeThrowingEggsRankController extends JeecgController<GameCampaignTypeThrowingEggsRank, IGameCampaignTypeThrowingEggsRankService> {

    @AutoLog(value = "砸蛋榜-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeThrowingEggsRank entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "砸蛋榜-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeThrowingEggsRank entity) {
        return super.add(entity);
    }

    @AutoLog(value = "砸蛋榜-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeThrowingEggsRank entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "砸蛋榜-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "砸蛋榜-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "砸蛋榜-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "XXX-导出") // TODO 
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeThrowingEggsRank entity) {
        return super.exportXls(request, entity, GameCampaignTypeThrowingEggsRank.class, "砸蛋榜");
    }

    @AutoLog(value = "XXX-导入") // TODO
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeThrowingEggsRank.class);
    }

}
