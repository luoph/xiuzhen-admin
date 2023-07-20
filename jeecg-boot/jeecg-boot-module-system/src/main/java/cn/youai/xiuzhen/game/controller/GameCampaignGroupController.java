package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.entity.GameCampaignGroup;
import cn.youai.xiuzhen.game.service.IGameCampaignGroupService;
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
 * @Description: 节日活动分组
 * @Author: jeecg-boot
 * @Date: 2023-07-20
 * @Version: V1.0
 */
@Slf4j
@RestController
@Api(tags = "节日活动分组")
@RequestMapping("/game/gameCampaignGroup")
public class GameCampaignGroupController extends JeecgController<GameCampaignGroup, IGameCampaignGroupService> {

    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignGroup entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "节日活动分组-添加")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:add")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignGroup entity) {
        return super.add(entity);
    }

    @AutoLog(value = "节日活动分组-编辑")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody GameCampaignGroup entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "节日活动分组-通过id删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:delete")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        return super.delete(id);
    }

    @AutoLog(value = "节日活动分组-批量删除")
    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        return super.deleteBatch(ids);
    }

    //@AutoLog(value = "节日活动分组-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return super.queryById(id);
    }

    //@RequiresPermissions("cn.youai.xiuzhen:game_campaign_group:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignGroup entity) {
        return super.exportXls(request, entity, GameCampaignGroup.class, "节日活动分组");
    }

    //@RequiresPermissions("game_campaign_group:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GameCampaignGroup.class);
    }

}
