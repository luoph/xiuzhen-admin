package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameCampaignTypeRecharge;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeRechargeService;
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
 * @description 充值活动
 * @date 2020-10-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeRecharge")
public class GameCampaignTypeRechargeController extends JeecgController<GameCampaignTypeRecharge, IGameCampaignTypeRechargeService> {

    @AutoLog(value = "充值活动-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeRecharge entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "充值活动-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeRecharge entity) {
        return super.add(entity);
    }

    @AutoLog(value = "充值活动-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeRecharge entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "充值活动-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "充值活动-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "充值活动-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "充值活动-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeRecharge entity) {
        return super.exportXls(request, entity, GameCampaignTypeRecharge.class, "充值活动");
    }

    @AutoLog(value = "充值活动-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignTypeRecharge.class);
    }

}
