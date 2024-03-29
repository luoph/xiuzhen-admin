package cn.youai.xiuzhen.game.controller;

import cn.youai.xiuzhen.game.constant.CampaignType;
import cn.youai.xiuzhen.game.entity.GameCampaign;
import cn.youai.xiuzhen.game.entity.GameCampaignTypeFallReward;
import cn.youai.xiuzhen.game.service.IGameCampaignService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeFallRewardService;
import cn.youai.xiuzhen.game.service.IGameCampaignTypeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jeecg-boot
 * @version V1.0
 * @description 掉落奖励组
 * @date 2021-01-15
 */
@Slf4j
@RestController
@RequestMapping("game/gameCampaignTypeFallReward")
public class GameCampaignTypeFallRewardController extends JeecgController<GameCampaignTypeFallReward, IGameCampaignTypeFallRewardService> {
    @Autowired
    private IGameCampaignService gameCampaignService;
    @Autowired
    private IGameCampaignTypeService gameCampaignTypeService;

    @AutoLog(value = "掉落奖励组-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GameCampaignTypeFallReward entity,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        return super.queryPageList(entity, pageNo, pageSize, req);
    }

    @AutoLog(value = "掉落奖励组-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GameCampaignTypeFallReward entity) {
        return super.add(entity);
    }

    @AutoLog(value = "掉落奖励组-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GameCampaignTypeFallReward entity) {
        return super.edit(entity);
    }

    @AutoLog(value = "掉落奖励组-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        return super.delete(id);
    }

    @AutoLog(value = "掉落奖励组-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        return super.deleteBatch(ids);
    }

    @AutoLog(value = "掉落奖励组-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return super.queryById(id);
    }

    @AutoLog(value = "掉落奖励组-导出")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GameCampaignTypeFallReward entity) {
        return super.exportXls(request, entity, GameCampaignTypeFallReward.class, CampaignType.FALL.getName() + "-掉落奖励组");
    }

    @AutoLog(value = "掉落奖励组-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(name = "campaignId") Long campaignId, @RequestParam(name = "typeId") Long typeId) {
        GameCampaign gameCampaign = gameCampaignService.getById(campaignId);
        if (null == gameCampaign) {
            return Result.error("找不到主活动配置");
        }
        return gameCampaignTypeService.importExcel(gameCampaign, typeId, request, CampaignType.FALL.getName() + "-掉落奖励组", service.getClass());
    }

}
