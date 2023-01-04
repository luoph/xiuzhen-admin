package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeFall;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeFallService;
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
 * @description 节日掉落
 * @date 2021-01-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeFall")
public class GameCampaignTypeFallController extends JeecgController<GameCampaignTypeFall, IGameCampaignTypeFallService> {

    @AutoLog(value = "节日掉落-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeFall entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "节日掉落-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeFall entity) {
        return super.add(entity);
    }

    @AutoLog(value = "节日掉落-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeFall entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "节日掉落-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "节日掉落-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "节日掉落-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "节日掉落-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeFall entity) {
        return super.exportXls(request, entity, GameCampaignTypeFall.class, CampaignType.valueOfServiceClass(service.getClass()).getName());
    }

    @AutoLog(value = "节日掉落-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeFall.class);
    }

}
